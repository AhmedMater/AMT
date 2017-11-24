package am.application;

import am.infrastructure.data.dto.LoginData;
import am.infrastructure.data.dto.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.UserIPFailure;
import am.infrastructure.data.hibernate.model.user.UserLoginLog;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.main.api.AMSecurityManager;
import am.main.api.AppConfigManager;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.repository.UserRepository;
import am.shared.enums.App_CC;
import am.shared.enums.EC;
import am.shared.enums.IC;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
public class UserService {
    private static final String CLASS = "UserService";

    @Inject private AppLogger logger;
    @Inject private UserRepository userRepository;
    @Inject private AMSecurityManager securityManager;
    @Inject private AppConfigManager appConfigManager;
    @Inject private DBManager dbManager;

    @Transactional
    public void register(AppSession appSession, UserRegisterData userData) throws Exception{
        String FN_NAME = "register";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, userData);

        if(userRepository.checkUsernameInDatabase(session, userData.getUsername()))
            throw new BusinessException(session, EC.AMT_0001, userData.getUsername());
        else
            logger.info(session, IC.AMT_0004, userData.getUsername());

        if(userRepository.checkEmailInDatabase(session, userData.getEmail()))
            throw new BusinessException(session, EC.AMT_0002, userData.getEmail());
        else
            logger.info(session, IC.AMT_0005, userData.getEmail());

        Users user = new Users();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setUsername(userData.getUsername());
        user.setPassword(securityManager.dm5Hash(session, userData.getPassword()));
        user.setEmail(userData.getEmail());
        user.setRole(new Role(Roles.STUDENT));
        user.setCreationDate(new Date());

        dbManager.persist(session, user, true);
        logger.info(session, IC.AMT_0007, user.getFullName());

        logger.endDebug(session);
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
            throw new BusinessException(session, EC.AMT_0008, username);
        }

        AuthenticatedUser authenticatedUser = null;
        BusinessException ex = null;

        String hashedPassword = securityManager.dm5Hash(session, password);
        UserIPDeActive userIPDeActive = userRepository.getUserIPDeActive(session, username, hashedPassword, loginUserIP);

        Integer MAX_LOGIN_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
        Integer LOGIN_DEACTIVATION_DURATION = appConfigManager.getConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);

        if(user.getPassword().equals(hashedPassword)){
            //Case: Username and Password are correct and the User is active for this IP
            if(userIPDeActive == null){
                authenticatedUser = correctAuthActive(session, user, hashedPassword, loginUserIP);
            }else{
                long diffInMin = (new Date().getTime() - userIPDeActive.getLastTrailDate().getTime()) / (60 * 1000);

                // Case: Correct Authentication, But IP Deactivated, and within the Deactivation time
                if(diffInMin < LOGIN_DEACTIVATION_DURATION){
                    ex = new BusinessException(session, EC.AMT_0016, LOGIN_DEACTIVATION_DURATION - diffInMin);
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

                ex = new BusinessException(session, EC.AMT_0014);
                UserLoginLog userLoginLog = new UserLoginLog(newUserIPDeActive, ex);
                dbManager.persist(session, userLoginLog, false);

            // Case: Wrong Password, But IP Deactivated
            }else if(!userIPDeActive.getActive()){
                userIPDeActive.incTrail();
                dbManager.merge(session, userIPDeActive, false);

                ex = new BusinessException(session, EC.AMT_0015, userIPDeActive.getTrailNum(), LOGIN_DEACTIVATION_DURATION);
                UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                dbManager.persist(session, userLoginLog, false);
            }else if(userIPDeActive.getActive()){

                // Case: Wrong Password, IP still Active, and within the MAX_LOGIN_TRAILS
                if(userIPDeActive.getTrailNum() < MAX_LOGIN_TRAILS-1){
                    userIPDeActive.incTrail();
                    dbManager.merge(session, userIPDeActive, false);

                    ex = new BusinessException(session, EC.AMT_0014);
                    UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                    dbManager.persist(session, userLoginLog, false);

                    // Case: Wrong Password, IP still Active, and passed the MAX_LOGIN_TRAILS
                }else{
                    userIPDeActive.deActiveIP();
                    dbManager.merge(session, userIPDeActive, false);

                    ex = new BusinessException(session, EC.AMT_0015, userIPDeActive.getTrailNum(), LOGIN_DEACTIVATION_DURATION);
                    UserLoginLog userLoginLog = new UserLoginLog(userIPDeActive, ex);
                    dbManager.persist(session, userLoginLog, false);
                }
            }
        }

        if(authenticatedUser != null) {
            logger.endDebug(session, authenticatedUser);
            return authenticatedUser;
        }else{
            logger.endDebug(session);
            throw ex;
        }
    }

    private AuthenticatedUser correctAuthActive(AppSession appSession, Users user, String hashedPassword, String ip) throws Exception{
        String FN_NAME = "correctAuthActive";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, user, (hashedPassword != null ? "Hashed Password":"Null"));

        String token = securityManager.generateToken(session, user.getUsername(), hashedPassword, new Date().getTime());
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, token);

        UserLoginLog userLoginLog = new UserLoginLog(user, ip);
        dbManager.persist(appSession, userLoginLog, false);

        logger.endDebug(session, authenticatedUser);
        return authenticatedUser;
    }
}
