package amt.testCases.user;

import am.infrastructure.data.dto.UserRegisterData;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AMSecurityManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.constants.Error;
import amt.common.constants.Resources;
import amt.common.constants.SQL;
import amt.common.generic.Repository;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.Date;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.SET_UP;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.*;
import static amt.common.constants.Rest.USER;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
@RunWith(Arquillian.class)
public class UserRegister {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private AMSecurityManager securityManager;

    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private UserRegisterData userData = new UserRegisterData("Ahmed", "Mater", 
            "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");

    private static final String CLASS = "UserRegister";

    private static String CLEARING_SCRIPT;
    private static String ROLE_SCRIPT;
    private static String REMOVE_ALL_USERS_SCRIPT;
    private static String INSERT_USER_SCRIPT;

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return amt.common.Deployment.createDeployment(Resources.USER);
    }

    @Before
    public void setUPTestCase(){
        try {
            if(CLEARING_SCRIPT == null)
                CLEARING_SCRIPT = Util.readResourceFile(SQL.CLEARING_SCRIPT);
            if(ROLE_SCRIPT == null)
                ROLE_SCRIPT = Util.readResourceFile(SQL.ROLE);
            if(REMOVE_ALL_USERS_SCRIPT == null)
                REMOVE_ALL_USERS_SCRIPT = Util.readResourceFile(SQL.REMOVE_USERS);
            if(INSERT_USER_SCRIPT == null)
                INSERT_USER_SCRIPT = Util.readResourceFile(SQL.NEW_USER);

            repository.executeScript(CLEARING_SCRIPT);
        } catch (Exception ex){
            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
        }
    }

    @After
    public void finishTestCase(){
        try {
            repository.executeScript(CLEARING_SCRIPT);
        } catch (Exception ex){
            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
        }
    }

    private void callRestForFormValidation(UserRegisterData data, FormValidation expected) throws Exception{
        Util.callRestForFormValidation(USER.RESOURCE, USER.REGISTER, data, expected);

        Users user = repository.getUserByUsername(data.getUsername());
        Assert.assertNull("User is found in Database", user);
    }

//    private void callRestForStringError(UserRegisterData data, String expectedError) throws Exception{
//        Util.callRestForStringError(USER.RESOURCE, USER.REGISTER, data, expectedError);
//
//        Users user = repository.getUserByUsername(data.getUsername());
//        Assert.assertNull("User is found in Database", user);
//    }

    private void checkDatabaseData(AppSession session, UserRegisterData expected) throws Exception{
        Response response = Util.restPOSTClient(USER.RESOURCE, USER.REGISTER, expected);
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

        repository.executeScript(REMOVE_ALL_USERS_SCRIPT);
    }

    @Test
    public void user_register_Valid_FirstName(){
        String TEST_CASE_NAME = "Registering-New-User_Valid_First-Name";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setFirstName("Ahmed");
            checkDatabaseData(session, validData);

            //Allow Hyphen
            validData.setFirstName("Ali-Amr");
            checkDatabaseData(session, validData);

            //Allow Comma
            validData.setFirstName("Ahmed,Amr");
            checkDatabaseData(session, validData);

            //Allow Period
            validData.setFirstName("Dr.Ahmed");
            checkDatabaseData(session, validData);

            //Allow Apostrophe
            validData.setFirstName("Dr'Ahmed");
            checkDatabaseData(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Valid_LastName(){
        String TEST_CASE_NAME = "Registering-New-User_Valid_Last-Name";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setLastName("Ahmed");
            checkDatabaseData(session, validData);

            //Allow Hyphen
            validData.setLastName("Ali-Amr");
            checkDatabaseData(session, validData);

            //Allow Comma
            validData.setLastName("Ahmed,Amr");
            checkDatabaseData(session, validData);

            //Allow Period
            validData.setLastName("Dr.Ahmed");
            checkDatabaseData(session, validData);

            //Allow Apostrophe
            validData.setLastName("Dr'Ahmed");
            checkDatabaseData(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Valid_Username(){
        String TEST_CASE_NAME = "Registering-New-User_Valid_Username";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setUsername("AhmedMater");
            checkDatabaseData(session, validData);

            // Allow Underscore
            validData.setUsername("Ahmed_Mater");
            checkDatabaseData(session, validData);

            //Allow Period
            validData.setUsername("Ahmed.Mater");
            checkDatabaseData(session, validData);

            //Allow Hyphen
            validData.setUsername("Ahmed-Mater");
            checkDatabaseData(session, validData);

            //Allow Numbers
            validData.setUsername("Ahmed123");
            checkDatabaseData(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Valid_Password(){
        String TEST_CASE_NAME = "Registering-New-User_Valid_Password";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setPassword("AhmedMater");
            checkDatabaseData(session, validData);

            // Allow Numbers
            validData.setPassword("Ahmed123");
            checkDatabaseData(session, validData);

            // Allow Period
            validData.setPassword("Ahmed.Mater");
            checkDatabaseData(session, validData);

            // Allow Hyphen
            validData.setPassword("Ahmed-Mater");
            checkDatabaseData(session, validData);

            // Allow Ampersand
            validData.setPassword("Ahmed@Mater");
            checkDatabaseData(session, validData);

            // Allow Underscore
            validData.setPassword("Ahmed_Mater");
            checkDatabaseData(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Valid_Email(){
        String TEST_CASE_NAME = "Registering-New-User_Email";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setEmail("ahmedmotair@gmail.com");
            checkDatabaseData(session, validData);

            // Allow Numbers
            validData.setEmail("ahmedmotair123@gmail.com");
            checkDatabaseData(session, validData);

            // Allow Period
            validData.setEmail("ahmed.motair@gizasystem.com");
            checkDatabaseData(session, validData);

            // Allow Hyphen
            validData.setEmail("ahmed-motair@gizasystems.com");
            checkDatabaseData(session, validData);

            //Allow Hyphen in Domain
            validData.setEmail("ahmedmotair@giza-systems.com");
            checkDatabaseData(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_FirstName(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-First-Name";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();
            
            // Test the Invalid Value
            invalidData.setFirstName("Ahm/ed");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Empty String Value
            invalidData.setFirstName("");
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Max Length String Value
            invalidData.setFirstName(Util.generateString(20));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Required Value
            invalidData.setFirstName(null);
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_LastName(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-Last-Name";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();

            // Test the Invalid Value
            invalidData.setLastName("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_LAST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Empty String Value
            invalidData.setLastName("");
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_LAST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Max Length String Value
            invalidData.setLastName(Util.generateString(55));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_LAST_NAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Required Value
            invalidData.setLastName(null);
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_LAST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_Username(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-Username";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();

            // Test the Invalid Value
            invalidData.setUsername("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_USERNAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Empty String Value
            invalidData.setUsername("");
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_USERNAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Min Length String Value
            invalidData.setUsername(Util.generateString(2));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Max Length String Value
            invalidData.setUsername(Util.generateString(55));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
            callRestForFormValidation(invalidData, expected);

            // Test the Required Value
            invalidData.setUsername(null);
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_Password(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-Password";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();

            // Test the Invalid Value
            invalidData.setPassword("Ah<med Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_PASSWORD);
            callRestForFormValidation(invalidData, expected);

            // Test the Empty String Value
            invalidData.setPassword("");
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_PASSWORD);
            callRestForFormValidation(invalidData, expected);

            // Test the Min Length String Value
            invalidData.setPassword(Util.generateString(2));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
            callRestForFormValidation(invalidData, expected);

            // Test the Max Length String Value
            invalidData.setPassword(Util.generateString(35));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
            callRestForFormValidation(invalidData, expected);

            // Test the Required Value
            invalidData.setPassword(null);
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_Email(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-Email";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();

            // Test the Invalid Value
            invalidData.setEmail("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_EMAIL);
            callRestForFormValidation(invalidData, expected);

            // Test the Empty String Value
            invalidData.setEmail("");
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_EMAIL);
            callRestForFormValidation(invalidData, expected);

            // Test the Min Length String Value
            invalidData.setEmail(Util.generateString(2));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
            callRestForFormValidation(invalidData, expected);

            // Test the Max Length String Value
            invalidData.setEmail(Util.generateString(105));
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
            callRestForFormValidation(invalidData, expected);

            // Test the Required Value
            invalidData.setEmail(null);
            expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_DuplicateUsername(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid-Duplicate-Username";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);
            repository.executeScript(INSERT_USER_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail("ahmedali@gmail.com");

            Util.callRestForStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_USER, invalidData.getUsername()));
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Invalid_DuplicateEmail(){
        String TEST_CASE_NAME = "Registering-New-User_Invalid_Duplicate-Email";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(ROLE_SCRIPT);
            repository.executeScript(INSERT_USER_SCRIPT);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername("ahmed_Ali");

            Util.callRestForStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_EMAIL, invalidData.getEmail()));
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
}
