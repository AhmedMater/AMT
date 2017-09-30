package am.repository;

import am.api.components.AppLogger;
import am.api.components.DBManager;
import am.infrastructure.data.hibernate.model.user.Users;
import am.session.AppSession;

import javax.inject.Inject;

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

        Users user = DBManager.getSingleResult(session, true,
                Users.class, Users.USER_NAME, username);

        logger.endDebug(session, user);
        return user;
    }
}
