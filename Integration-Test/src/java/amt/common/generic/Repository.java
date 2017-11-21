package amt.common.generic;

import am.infrastructure.data.hibernate.model.user.Users;
import am.shared.common.SharedParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
public class Repository {

    @PersistenceContext(unitName = SharedParam.PERSISTENCE_UNIT)
    private EntityManager em;

    @Transactional
    public void executeScript(String script) throws Exception{
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
