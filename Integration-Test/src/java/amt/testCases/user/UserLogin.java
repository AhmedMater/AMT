package amt.testCases.user;

import am.infrastructure.data.dto.LoginData;
import am.infrastructure.data.dto.UserRegisterData;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AMSecurityManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Rest;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.Date;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;

/**
 * Created by ahmed.motair on 11/22/2017.
 */
@RunWith(Arquillian.class)
public class UserLogin {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private DataGenerator dataGenerator;
    @Inject private AMSecurityManager securityManager;

    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private LoginData loginData = new LoginData("Ahmed_Mater", "123456");
    private UserRegisterData userData = new UserRegisterData("Ahmed", "Mater",
            "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");

    private static final String CLASS = "UserLogin";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

//    @Test @InSequence(1)
//    public void beforeClass(){
//        try {
////            repository.executeScript(S);
//        } catch (Exception ex){
//            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
//        }
//    }

    private void callRestForFormValidation(UserRegisterData data, FormValidation expected) throws Exception{
        Util.callRestForFormValidation(Rest.USER.RESOURCE, Rest.USER.REGISTER, data, expected);

        Users user = repository.getUserByUsername(data.getUsername());
        Assert.assertNull("User is found in Database", user);
    }

    private void checkDatabaseData(AppSession session, UserRegisterData expected) throws Exception{
        Response response = Util.restPOSTClient(Rest.USER.RESOURCE, Rest.USER.REGISTER, expected);
        Assert.assertEquals("Response Status failed", Response.Status.OK.getStatusCode(), response.getStatus());

        Users actual = repository.getUserByUsername(expected.getUsername());
        Assert.assertNotNull("User isn't found in Database", actual);

        Assert.assertEquals("First name failed", expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals("Last name failed", expected.getLastName(), actual.getLastName());
        Assert.assertEquals("Username failed", expected.getUsername(), actual.getUsername());
        Assert.assertEquals("Password failed", securityManager.dm5Hash(session, expected.getPassword()), actual.getPassword());
        Assert.assertEquals("Email failed", expected.getEmail(), actual.getEmail());
        Assert.assertEquals("Role failed", "St", actual.getRole().getRole());

        if(!Util.isEqualDates(new Date(), actual.getCreationDate()))
            Assert.fail("Creation Date failed");

        // Clearing the Users Table
        repository.executeScript(Scripts.CLEARING_USER_TABLE);
    }

    @Test
    public void user_login_Valid_Username(){
        String TEST_CASE_NAME = "Login-User_Valid_Username";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            dataGenerator.registerUser(userData);

            LoginData validData = this.loginData.clone();
            dataGenerator.login(validData);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

//    @Test
//    public void user_login_Valid_Password(){
//        String TEST_CASE_NAME = "Login-User_Valid_Password";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            repository.executeScript(INSERT_NEW_USERS_SCRIPT);
//
//            LoginData validData = this.loginData.clone();
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test
//    public void user_login_InValid_Username(){
//        String TEST_CASE_NAME = "Login-User_Valid_Username";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
////            repository.executeScript(ROLE_SCRIPT);
//
//            LoginData validData = this.loginData.clone();
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test
//    public void user_login_InValid_Password(){
//        String TEST_CASE_NAME = "Login-User_Valid_Password";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
////            repository.executeScript(ROLE_SCRIPT);
//
//            LoginData validData = this.loginData.clone();
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test
//    public void user_login_Wrong_Username(){
//        String TEST_CASE_NAME = "Login-User_Valid_Password";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
////            repository.executeScript(ROLE_SCRIPT);
//
//            LoginData validData = this.loginData.clone();
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test
//    public void user_login_Wrong_Password_FirstTime(){
//        String TEST_CASE_NAME = "Login-User_Valid_Password";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
////            repository.executeScript(ROLE_SCRIPT);
//
//            LoginData validData = this.loginData.clone();
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
}
