package am.repository;

import am.infrastructure.data.hibernate.model.lookup.Role;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.session.AppSession;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed.motair on 11/24/2017.
 */
public class LookupRepository {
    private static final String CLASS = "LookupRepository";
    @Inject private DBManager dbManager;
    @Inject private AppLogger logger;

    public List<Role> getAllNonAdminRoles(AppSession session) throws Exception{
        String FN_NAME = "getAllNonAdminRoles";
        logger.startDebug(session);
        List<Role> roleList = new ArrayList<>();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Role.IS_ADMIN, false);

        roleList = dbManager.getList(session, true, Role.class, parameters);

        logger.endDebug(session, roleList);
        return roleList;
    }

//    public Role getNonAdminRole(AppSession am.shared.session, String newRole) throws Exception{
//        String FN_NAME = "getNonAdminRole";
//        logger.startDebug(am.shared.session);
//        Role role = new Role();
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put(Role.IS_ADMIN, false);
//
//        try{
//            role = dbManager.getSingleResult(am.shared.session, true, Role.class, parameters);
//        }catch (DBException ex){
//            if(ex.getAME_CODE().equals(AME.DB_016)) {
//                logger.error(am.shared.session, ex);
//                return null;
//            }else
//                throw ex;
//        }
//
//        logger.endDebug(am.shared.session, role);
//        return role;
//    }
}
