package amt.common.generic;

import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.UserIPFailure;
import am.infrastructure.data.hibernate.model.user.UserLoginLog;
import am.infrastructure.data.hibernate.model.user.Users;
import amt.common.enums.Scripts;
import org.junit.Assert;

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

    @PersistenceContext(unitName = "AM")
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
            if(query.trim().isEmpty())
                continue;

            Query q = em.createNativeQuery(query);
            q.executeUpdate();
            System.out.println(MessageFormat.format("Query: {0} is executed successfully", query));
        }
    }

    public Users getUserByUsername(String username){
        List<Users> list = em.createQuery("FROM Users WHERE username = :Username", Users.class)
                .setParameter("Username", username).getResultList();
        return (list.size() > 0) ? list.get(0) : null;
    }

    public List<UserLoginLog> getUserLoginLog(Integer userID) {
        List<UserLoginLog> list = em
                .createQuery("FROM UserLoginLog WHERE user.userID = :UserID", UserLoginLog.class)
                .setParameter("UserID", userID).getResultList();

        Assert.assertTrue("UserLoginLog : " + userID + " isn't found in Database", list.size() != 0);
        return list;
    }

    public UserIPFailure getUserIPFailure(String username) {
        List<UserIPFailure> list = em.createQuery("FROM UserIPFailure WHERE username = :Username", UserIPFailure.class)
                .setParameter("Username", username).getResultList();

        Assert.assertTrue("UserIPFailure: " + username + " isn't found in Database", list.size() == 1);
        return list.get(0);
    }

    public UserIPDeActive getUserIPDeActive(Integer userID) {
        List<UserIPDeActive> list = em
                .createQuery("FROM UserIPDeActive WHERE user.userID = :UserID", UserIPDeActive.class)
                .setParameter("UserID", userID).getResultList();

        Assert.assertTrue("UserIPDeActive : " + userID + " isn't found in Database", list.size() == 1);
        return list.get(0);
    }
}
