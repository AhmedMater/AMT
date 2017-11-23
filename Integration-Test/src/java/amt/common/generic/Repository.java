package amt.common.generic;

import am.infrastructure.data.hibernate.model.user.Users;
import am.shared.common.SharedParam;
import amt.common.enums.Scripts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
public class Repository {
    private static Map<Scripts, String> scripts = new HashMap<>();

    @PersistenceContext(unitName = SharedParam.PERSISTENCE_UNIT)
    private EntityManager em;

    @Transactional
    public void executeScript(Scripts scriptName) throws Exception{
        String script = scripts.get(scriptName);

        if(script == null){
            script = scriptName.readScript();
            scripts.put(scriptName, script);
        }

        String[] queries = script.split(";");
        for (String query :queries) {
            Query q = em.createNativeQuery(query);
            q.executeUpdate();
            System.out.println(MessageFormat.format("Query: {0} is executed successfully", query));
        }
    }

    public Users getUserByUsername(String username){
        List<Users> list = em.createQuery("FROM Users WHERE username = :Username", Users.class)
                .setParameter("Username", username).getResultList();

        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
