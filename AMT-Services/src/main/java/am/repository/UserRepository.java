package am.repository;

import am.api.components.AppLogger;
import am.api.components.db.DBManager;
import am.common.enums.AME;
import am.exception.DBException;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.Users;
import am.session.AppSession;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
public class UserRepository {
    private static final String CLASS = "UserRepository";
    @Inject private DBManager DBManager;
    @Inject private AppLogger logger;

    public Boolean checkUsernameInDatabase(AppSession appSession, String username) throws Exception{
        String FN_NAME = "checkUsernameInDatabase";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username);

        Boolean result = DBManager.checkIsFound(session, true, Users.USER_ID,
                Users.class, Users.USER_NAME, username);

        logger.endDebug(session, result);
        return result;
    }

    public Boolean checkEmailInDatabase(AppSession appSession, String email) throws Exception{
        String FN_NAME = "checkEmailInDatabase";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, email);

        Boolean result = DBManager.checkIsFound(session, true, Users.USER_ID,
                Users.class, Users.EMAIL, email);

        logger.endDebug(session, result);
        return result;
    }

    public Users getUserByUserName(AppSession appSession, String username) throws Exception {
        String FN_NAME = "getUserByUserName";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Users.USER_NAME, username);

        Users user = getUser(session, parameters);
        logger.endDebug(session, user);
        return user;
    }

    public Users getUserByUsernameAndPassword(AppSession appSession, String username, String hashedPassword) throws Exception {
        String FN_NAME = "getUserByUsernameAndPassword";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username, (hashedPassword != null ? "Hashed Password" : "Null"));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Users.USER_NAME, username);
        parameters.put(Users.PASSWORD, hashedPassword);

        Users user = getUser(session, parameters);
        logger.endDebug(session, user);
        return user;
    }

    public UserIPDeActive checkUsernameIsDeactivated(AppSession appSession, String username, String hashedPassword, String ip) throws Exception{
        String FN_NAME = "checkUsernameDeactivated";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username, (hashedPassword != null ? "Hashed Password" : "Null"));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(UserIPDeActive.USER_NAME, username);
        parameters.put(UserIPDeActive.PASSWORD, hashedPassword);
        parameters.put(UserIPDeActive.IP, ip);
        parameters.put(UserIPDeActive.IS_ACTIVE, false);

        UserIPDeActive user = null;
        try {
            user = DBManager.getSingleResult(session, true, UserIPDeActive.class, parameters);
        }catch (DBException ex){
            if(ex.getAME_CODE().equals(AME.DB_016)) {
                logger.error(session, ex);
                return null;
            }else
                throw ex;
        }

        logger.endDebug(session, user);
        return user;
    }
    private Users getUser(AppSession appSession, Map<String, Object> parameters) throws Exception{
        String FN_NAME = "getUser";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);

        Users user = null;
        try {
            user = DBManager.getSingleResult(session, true, Users.class, parameters);
//            user.setPassword(null);
        }catch (DBException ex){
            if(ex.getAME_CODE().equals(AME.DB_016)) {
                logger.error(session, ex);
                return null;
            }else
                throw ex;
        }
        return user;
    }

}
