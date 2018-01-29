package am.repository;

import am.infrastructure.data.dto.filters.UserListFilter;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.ui.UserListUI;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.api.db.HQLCondition;
import am.main.api.db.QueryBuilder;
import am.main.data.dto.ListResultSet;
import am.main.exception.DBException;
import am.main.session.AppSession;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static am.infrastructure.generic.ConfigParam.MAX_PAGE_SIZE;
import static am.main.data.enums.Operators.EQ;
import static am.main.data.enums.Operators.LIKE;
import static am.main.data.enums.impl.IEC.E_DB_16;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
public class UserRepository {
    private static final String CLASS = "UserRepository";
    @Inject private DBManager dbManager;
    @Inject private AppLogger logger;

    public Boolean checkUsernameInDatabase(AppSession appSession, String username) throws Exception{
        String FN_NAME = "checkUsernameInDatabase";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username);

        Boolean result = dbManager.checkIsFound(session, true, Users.USER_ID,
                Users.class, Users.USER_NAME, username);

        logger.endDebug(session, result);
        return result;
    }

    public Boolean checkEmailInDatabase(AppSession appSession, String email) throws Exception{
        String FN_NAME = "checkEmailInDatabase";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, email);

        Boolean result = dbManager.checkIsFound(session, true, Users.USER_ID,
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

    public UserIPDeActive getUserIPDeActive(AppSession appSession, String username, String hashedPassword, String ip) throws Exception{
        String FN_NAME = "getUserIPDeActive";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, username, (hashedPassword != null ? "Hashed Password" : "Null"), ip);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(UserIPDeActive.USER_NAME, username);
        parameters.put(UserIPDeActive.IP, ip);

        UserIPDeActive user = null;
        try {
            user = dbManager.getSingleResult(session, true, UserIPDeActive.class, parameters);
        }catch (DBException ex){
            if(ex.getCode().equals(E_DB_16)) {
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
            user = dbManager.getSingleResult(session, false, Users.class, parameters);
        }catch (DBException ex){
            if(ex.getCode().equals(E_DB_16)) {
                logger.error(session, ex);
                return null;
            }else
                throw ex;
        }
        return user;
    }

    @Transactional
    public ListResultSet<UserListUI> getAllUser(AppSession session, UserListFilter filters){
        String FN_NAME = "getAllUser";
        logger.startDebug(session, filters);

        Date myDate = new Date();

        String selectData = "SELECT new am.infrastructure.data.view.ui.UserListUI(userID, " +
                "concat(firstName, concat(' ', lastName)), role.role, creationDate)";

        String from = "FROM Users";

        QueryBuilder<UserListUI> queryBuilder = new QueryBuilder<UserListUI>(UserListUI.class, logger, session);
        queryBuilder.setDataSelect(selectData);
        queryBuilder.setFrom(from);

        String realNameAttribute = "concat(" + Users.FIRST_NAME + ", concat(' ', " + Users.LAST_NAME + "))";
        queryBuilder.addCondition(new HQLCondition<String>(realNameAttribute, LIKE, filters.getRealName()));
        queryBuilder.addCondition(new HQLCondition<String>(Users.ROLE, EQ, filters.getRole()));
        queryBuilder.addCondition(new HQLCondition<Date>(filters.getCreationDateFrom(), filters.getCreationDateTo(), Users.CREATION_DATE));

        queryBuilder.setSorting(filters.getSorting());
        queryBuilder.setPagingInfo(filters.getPageNum(), MAX_PAGE_SIZE);

        EntityManager em = dbManager.getUnCachedEM();
        queryBuilder.executeQuery(em);

        ListResultSet<UserListUI> resultSet = queryBuilder.getResultSet();
        logger.endDebug(session, resultSet);
        return resultSet;
    }
}
