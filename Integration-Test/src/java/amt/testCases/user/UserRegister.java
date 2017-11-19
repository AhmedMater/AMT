package amt.testCases.user;

import am.infrastructure.data.dto.UserRegisterData;
import amt.common.constants.Resources;
import amt.common.generic.Repository;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;

import static amt.common.constants.Error.SET_UP;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Rest.USER;
import static amt.common.constants.SQL.CLEARING_SCRIPT;
import static amt.common.constants.SQL.ROLE;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
@RunWith(Arquillian.class)
public class UserRegister {
    @Inject private Repository repository;

    @Deployment
    public static WebArchive createDeployment() {
        return amt.common.Deployment.createDeployment(Resources.USER);
    }

//    @BeforeClass
//    public void setUPClass(){
//        try {
//            String clearingScript = Util.readResourceFile(CLEARING_SCRIPT);
//            repository.executeScript(clearingScript);
//        } catch (Exception ex){
//            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
//        }
//    }

    @Before
    public void setUPTestCase(){
        try {
            String clearingScript = Util.readResourceFile(CLEARING_SCRIPT);
            repository.executeScript(clearingScript);
        } catch (Exception ex){
            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
        }
    }

    @After
    public void finishTestCase(){
        try {
            String clearingScript = Util.readResourceFile(CLEARING_SCRIPT);
            repository.executeScript(clearingScript);
        } catch (Exception ex){
            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
        }
    }

    @Test
    public void user_register_bestCase(){
        String TEST_CASE_NAME = "Registering New User";
        try{
            String roleScript = Util.readResourceFile(ROLE);
            repository.executeScript(roleScript);

            UserRegisterData data = new UserRegisterData();
            data.setFirstName("Ahmed");
            data.setLastName("Mater");
            data.setEmail("ahmed.motair@gizasystem.com");
            data.setUsername("Ahmed_Mater");
            data.setPassword("123456");

            Response response = Util.restPOSTClient(USER.RESOURCE, USER.REGISTER, data);

            if(response.getStatus() != Response.Status.OK.getStatusCode())
                Assert.fail(MessageFormat.format("User Register Test Case failed, Response Status: {0}, " +
                        "Error: {1}", response.getStatus(), response.readEntity(String.class)));

        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
}
