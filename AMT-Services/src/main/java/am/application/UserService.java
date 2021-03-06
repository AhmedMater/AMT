package am.application;

import am.infrastructure.am.impl.ASC;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.UserIPFailure;
import am.infrastructure.data.hibernate.model.user.UserLoginLog;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.infrastructure.data.view.UserProfileData;
import am.infrastructure.data.view.lookup.list.UserListLookup;
import am.main.api.AppLogger;
import am.main.api.ConfigManager;
import am.main.api.SecurityManager;
import am.main.api.db.DBManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.repository.CourseRepository;
import am.repository.LookupRepository;
import am.repository.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static am.infrastructure.am.impl.ASE.*;
import static am.infrastructure.am.impl.ASI.I_USR_3;
import static am.infrastructure.am.impl.ASI.I_USR_4;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
public class UserService {
    private static final String CLASS = "UserService";

    @Inject private AppLogger logger;
    @Inject private UserRepository userRepository;
    @Inject private SecurityManager securityManager;
    @Inject private ConfigManager configManager;
    @Inject private DBManager dbManager;
    @Inject private LookupRepository lookupRepository;
    @Inject private CourseRepository courseRepository;

    @Transactional
    public Users register(AppSession appSession, UserRegisterData userData, Roles role) throws Exception{
        String FN_NAME = "register";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, userData);

        if(userRepository.checkUsernameInDatabase(session, userData.getUsername()))
            throw new BusinessException(session, E_USR_1, userData.getUsername());
        else
            logger.info(session, I_USR_3, userData.getUsername());

        if(userRepository.checkEmailInDatabase(session, userData.getEmail()))
            throw new BusinessException(session, E_USR_2, userData.getEmail());
        else
            logger.info(session, I_USR_4, userData.getEmail());

        Users user = new Users();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setUsername(userData.getUsername());
        user.setPassword(securityManager.dm5Hash(session, userData.getPassword()));
        user.setEmail(userData.getEmail());
        user.setRole(new Role(role));
        user.setCreationDate(new Date());

        user = dbManager.persist(session, user, true);
        logger.endDebug(session, user);
        return user;
    }

    @Transactional(dontRollbackOn = BusinessException.class)
    public AuthenticatedUser login(AppSession appSession, LoginData loginData, String loginUserIP) throws Exception{
        String FN_NAME = "login";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, loginData);

        String username = loginData.getUsername();
        String password = loginData.getPassword();

        Users user = userRepository.getUserByUserName(session, username);

        //Case: Username isn't found in the System
        if(user == null) {
            UserIPFailure userIPFailure = new UserIPFailure(username, loginUserIP);
            dbManager.persist(session, userIPFailure, false);
            throw new BusinessException(session, E_USR_4, username);
        }

        AuthenticatedUser authenticatedUser = null;
        BusinessException ex = null;

        String hashedPassword = securityManager.dm5Hash(session, password);
        UserIPDeActive userIPDeActive = userRepository.getUserIPDeActive(session, username, hashedPassword, loginUserIP);

        Integer MAX_LOGIN_TRAILS = configManager.getConfigValue(session, ASC.MAX_LOGIN_TRAILS, Integer.class);
        Integer LOGIN_DEACTIVATION_DURATION = configManager.getConfigValue(session, ASC.LOGIN_ACTIVATION_MIN, Integer.class);

        if(user.getPassword().equals(hashedPassword)){
            //Case: Username and Password are correct and the User is active for this IP
            if(userIPDeActive == null){
                authenticatedUser = correctAuthActive(session, user, hashedPassword, loginUserIP);
            }else{
                long diffInMin = (new Date().getTime() - userIPDeActive.getLastTrailDate().getTime()) / (60 * 1000);

                // Case: Correct Authentication, But IP Deactivated, and within the Deactivation time
                if(diffInMin < LOGIN_DEACTIVATION_DURATION){
                    ex = new BusinessException(session, E_USR_5, LOGIN_DEACTIVATION_DURATION - diffInMin);
                    UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                    dbManager.persist(session, userLoginLog, false);

                // Case: Correct Authentication, But IP Deactivated, and passed the Deactivation time
                }else{
                    userIPDeActive.activeIP();
                    dbManager.merge(session, userIPDeActive, false);
                    authenticatedUser = correctAuthActive(session, userIPDeActive.getUser(), hashedPassword, userIPDeActive.getIp());
                }
            }
        }else{
            // Case: Wrong Password, and new IP Address
            if(userIPDeActive == null){
                UserIPDeActive newUserIPDeActive = new UserIPDeActive(user, loginUserIP);
                dbManager.persist(session, newUserIPDeActive, false);

                ex = new BusinessException(session, E_USR_6);
                UserLoginLog userLoginLog = new UserLoginLog(newUserIPDeActive, ex);
                dbManager.persist(session, userLoginLog, false);

            // Case: Wrong Password, But IP Deactivated
            }else if(!userIPDeActive.getActive()){
                userIPDeActive.incTrail();
                dbManager.merge(session, userIPDeActive, false);

                ex = new BusinessException(session, E_USR_7, userIPDeActive.getTrailNum(), LOGIN_DEACTIVATION_DURATION);
                UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                dbManager.persist(session, userLoginLog, false);
            }else if(userIPDeActive.getActive()){

                // Case: Wrong Password, IP still Active, and within the MAX_LOGIN_TRAILS
                if(userIPDeActive.getTrailNum() < MAX_LOGIN_TRAILS-1){
                    userIPDeActive.incTrail();
                    dbManager.merge(session, userIPDeActive, false);

                    ex = new BusinessException(session, E_USR_6);
                    UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                    dbManager.persist(session, userLoginLog, false);

                    // Case: Wrong Password, IP still Active, and passed the MAX_LOGIN_TRAILS
                }else{
                    userIPDeActive.deActiveIP();
                    dbManager.merge(session, userIPDeActive, false);

                    ex = new BusinessException(session, E_USR_7, userIPDeActive.getTrailNum(), LOGIN_DEACTIVATION_DURATION);
                    UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                    dbManager.persist(session, userLoginLog, false);
                }
            }
        }

        if(authenticatedUser != null) {
            logger.endDebug(session, authenticatedUser);
            return authenticatedUser;
        }else
            throw ex;
    }

    private AuthenticatedUser correctAuthActive(AppSession appSession, Users user, String hashedPassword, String ip) throws Exception{
        String FN_NAME = "correctAuthActive";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, user, (hashedPassword != null ? "Hashed Password":"Null"));

        String token = securityManager.generateAccessToken(session, user.getUsername(), hashedPassword, new Date().getTime());
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);

        UserLoginLog userLoginLog = new UserLoginLog(user, ip);
        dbManager.persist(appSession, userLoginLog, false);

        logger.endDebug(session, authenticatedUser);
        return authenticatedUser;
    }

//    public void changeRole(AppSession appSession, String newRole, String ownerUserID) throws Exception{
//        String FN_NAME = "changeRole";
//        AppSession session = appSession.updateSession(CLASS, FN_NAME);
//        logger.startDebug(session, newRole, ownerUserID);
//
//        Integer ownerID;
//        try {
//            ownerID = new Integer(ownerUserID);
//        }catch (Exception ex){
//            throw new BusinessException(session, EC.AMT_0023, ownerUserID);
//        }
//
//        Users profileOwner = dbManager.find(session, Users.class, ownerID, false);
//        if(profileOwner == null)
//            throw new BusinessException(session, EC.AMT_0023, ownerUserID);
//        else if(profileOwner.getRole().isAdmin())
//            throw new BusinessException(session, EC.AMT_0026);
//        else if(profileOwner.getRole().getRole().equals(Roles.TUTOR.role()) &&
//                courseRepository.userHasCourses(session, profileOwner.getUserID()))
//            throw new BusinessException(session, EC.AMT_0029);
//
//        Role role = dbManager.find(session, Role.class, newRole, true);
//        if(role == null)
//            throw new BusinessException(session, EC.AMT_0002, Role.class.getSimpleName(), newRole);
//        else if(role.isAdmin())
//            throw new BusinessException(session, EC.AMT_0027);
//
//        profileOwner.setRole(role);
//        dbManager.merge(session, profileOwner, false);
//
//        logger.endDebug(session);
//    }

    public UserProfileData getProfileData(AppSession appSession, String ownerUserID, Users viewerUser) throws Exception{
        String FN_NAME = "getProfileData";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, ownerUserID, viewerUser);

        Integer ownerID;
        try {
            ownerID = new Integer(ownerUserID);
        }catch (Exception ex){
            logger.error(session, ex);
            throw new BusinessException(session, E_USR_9, ownerUserID);
        }

        Users profileOwner = dbManager.find(session, Users.class, ownerID, false);
        if(profileOwner == null)
            throw new BusinessException(session, E_USR_10, ownerID);

        UserProfileData profileData = new UserProfileData(profileOwner);

        if(ownerID.equals(viewerUser.getUserID())){
            profileData.setCanEdit(true);
            profileData.setCanUpgradeRole(false);
        }else if(viewerUser.getRole().isAdmin() && !profileOwner.getRole().isAdmin()){
            profileData.setCanEdit(false);
            profileData.setCanUpgradeRole(true);
            profileData.setRoleList(lookupRepository.getAllNonAdminRoles(session));
        }else {
            profileData.setCanEdit(false);
            profileData.setCanUpgradeRole(false);
        }

        logger.endDebug(session, profileData);
        return profileData;
    }

    public UserListLookup getUserListLookup(AppSession appSession) throws Exception{
        String FN_NAME = "getUserListLookup";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session);

        UserListLookup result = new UserListLookup();

        List<Role> roleList = dbManager.findAll(session, Role.class, true);
        result.setRoles(roleList);

        logger.endDebug(session, result);
        return result;
    }
}
