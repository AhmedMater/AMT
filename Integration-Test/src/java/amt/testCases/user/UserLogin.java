package amt.testCases.user;

import am.application.SecurityService;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.UserIPFailure;
import am.infrastructure.data.hibernate.model.user.UserLoginLog;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.main.api.ConfigManager;
import am.main.api.validation.FormValidation;
import am.main.common.RegExp;
import am.main.session.AppSession;
import am.shared.enums.App_CC;
import am.shared.enums.EC;
import am.shared.enums.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
import amt.common.constants.Rest;
import amt.common.constants.Rest.USER;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.RestUtil;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static am.shared.enums.Interface.ARQUILLIAN;
import static am.shared.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.*;

/**
 * Created by ahmed.motair on 11/22/2017.
 */
@RunWith(Arquillian.class)
public class UserLogin {
    @Inject private Repository repository;
    @Inject private SecurityService securityService;
    @Inject private ConfigManager configManager;
    @Inject private DataGenerator dataGenerator;

    private static final Integer USER_ID = 3;
    private static final String USERNAME = "Ahmed_Mater";
    private static final String PASSWORD = "123456";

    private static final String CLASS = "UserLogin";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.AMTServicesWAR(Scripts.getAllScripts());
    }

    @Test @InSequence(1)
    public void startClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }

    @Test @InSequence(2)
    public void user_login_CorrectAuthentication_ActiveIP(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_ActiveIP";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData loginData = new LoginData(USERNAME, PASSWORD);

            Response response = RestUtil.post(Rest.USER.RESOURCE, Rest.USER.LOGIN, loginData);
            Assert.assertEquals("Register New User failed", Response.Status.OK.getStatusCode(), response.getStatus());

            AuthenticatedUser actual = response.readEntity(AuthenticatedUser.class);
            Assert.assertNotNull("Authenticated User isn't returned", actual);

            Users user = repository.getUserByUsername(loginData.getUsername());

            Assert.assertEquals("Full Name isn't correct", user.getFullName(), actual.getFullName());
            Assert.assertEquals("User ID isn't correct", user.getUserID(), actual.getUserID());
            Assert.assertEquals("User Role isn't correct", user.getRole().getRole(), actual.getRole());
            Assert.assertNotNull("User Token isn't generated", actual.getToken());
            Assert.assertNotNull("User Token isn't valid", securityService.validateToken(session, actual.getToken()));

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", loginData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertTrue("Success isn't correct", loginLog.getSuccess());
            Assert.assertNull("Error Code isn't null", loginLog.getErrorCode());
            Assert.assertNull("Error Message isn't null", loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(3)
    public void user_login_UsernameIsNotFound(){
        String TEST_CASE_NAME = "user_login_UsernameIsNotFound";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData("Ahmed_Ali", PASSWORD);

            Util.postStringError(USER.RESOURCE, USER.LOGIN,
                    invalidData, MessageFormat.format(USER_IS_NOT_FOUND, invalidData.getUsername()));

            UserIPFailure userIPFailure = repository.getUserIPFailure(invalidData.getUsername());
            Assert.assertNotNull("IP isn't null", userIPFailure.getIp());
            Util.isEqualDates(new Date(), userIPFailure.getLoginDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(4)
    public void user_login_WrongPassword_NewIP(){
        String TEST_CASE_NAME = "user_login_WrongPassword_NewIP";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0014.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", WRONG_PASSWORD, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(USER_ID);
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(1), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(5)
    public void user_login_WrongPassword_ActiveIP_LessThanMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_LessThanMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Integer MAX_TRAILS = configManager.getConfigValue(App_CC.MAX_LOGIN_TRAILS, Integer.class);
            int numOfTrails = MAX_TRAILS-1;

            for (int i = 0; i <numOfTrails; i++)
                Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", numOfTrails, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0014.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", WRONG_PASSWORD, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(USER_ID);
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(numOfTrails), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(6)
    public void user_login_WrongPassword_ActiveIP_EqualsMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_EqualsMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Integer MAX_TRAILS = configManager.getConfigValue(App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer LOGIN_DEACTIVATION_DURATION = configManager.getConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS.intValue(), loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0015.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", expectedErrorMsg, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(USER_ID);
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", MAX_TRAILS, userIPDeActive.getTrailNum());
            Assert.assertFalse("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(7)
    public void user_login_WrongPassword_ActiveIP_GreaterThanMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_LessThanMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Integer MAX_TRAILS = configManager.getConfigValue(App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer LOGIN_DEACTIVATION_DURATION = configManager.getConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS + 1, LOGIN_DEACTIVATION_DURATION);
            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", numOfTrails, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0015.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", expectedErrorMsg, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(USER_ID);
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(numOfTrails), userIPDeActive.getTrailNum());
            Assert.assertFalse("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(8)
    public void user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Integer LOGIN_DEACTIVATION_DURATION = 3;

            Integer MAX_TRAILS = configManager.getConfigValue(App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer OLD_LOGIN_DEACTIVATION_DURATION = configManager.getConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);
            configManager.updateConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, LOGIN_DEACTIVATION_DURATION.toString());

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            LoginData validData = new LoginData(USERNAME, PASSWORD);

            Thread.sleep(65 * 1000);

            Util.postStringError(USER.RESOURCE, USER.LOGIN, validData,
                    MessageFormat.format(WRONG_PASSWORD_LT_LOGIN_DEACTIVATION_DURATION, LOGIN_DEACTIVATION_DURATION-1));

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS + 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0016.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct",
                    MessageFormat.format(WRONG_PASSWORD_LT_LOGIN_DEACTIVATION_DURATION, LOGIN_DEACTIVATION_DURATION-1), loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            configManager.updateConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, OLD_LOGIN_DEACTIVATION_DURATION.toString());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(9)
    public void user_login_CorrectAuthentication_DeActiveIP_GreaterThanMaxDeactivateDuration(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_USER);

            LoginData invalidData = new LoginData(USERNAME, "12advf34");

            Integer LOGIN_DEACTIVATION_DURATION = 1;

            Integer MAX_TRAILS = configManager.getConfigValue(App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer OLD_LOGIN_DEACTIVATION_DURATION = configManager.getConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);
            configManager.updateConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, LOGIN_DEACTIVATION_DURATION.toString());

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.postStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            LoginData validData = new LoginData(USERNAME, PASSWORD);

            Thread.sleep(60 * 1000);
            dataGenerator.login(validData);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(USER_ID);
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS + 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", validData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertTrue("Success isn't correct", loginLog.getSuccess());
            Assert.assertNull("Error Code isn't null", loginLog.getErrorCode());
            Assert.assertNull("Error Message isn't null", loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(USER_ID);
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(1), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            configManager.updateConfigValue(App_CC.LOGIN_ACTIVATE_MINUTES, OLD_LOGIN_DEACTIVATION_DURATION.toString());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(10)
    public void user_login_ValidLoginData(){
        String TEST_CASE_NAME = "user_login_ValidLoginData";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.LOGIN_DATA_VALID);

            LoginData loginData = new LoginData("amr.123-Mater_Ali_amr.123-Mater_Ali_amr.123-Mater5",
                    "ahmed@amr.ali-moh123@Mater_Ali");

            Response response = RestUtil.post(Rest.USER.RESOURCE, Rest.USER.LOGIN, loginData);
            Assert.assertEquals("Register New User failed", Response.Status.OK.getStatusCode(), response.getStatus());

            AuthenticatedUser actual = response.readEntity(AuthenticatedUser.class);
            Assert.assertNotNull("Authenticated User isn't returned", actual);

            Users user = repository.getUserByUsername(loginData.getUsername());

            Assert.assertEquals("Full Name isn't correct", user.getFullName(), actual.getFullName());
            Assert.assertEquals("User ID isn't correct", user.getUserID(), actual.getUserID());
            Assert.assertEquals("User Role isn't correct", user.getRole().getRole(), actual.getRole());
            Assert.assertNotNull("User Token isn't generated", actual.getToken());
            Assert.assertNotNull("User Token isn't valid", securityService.validateToken(session, actual.getToken()));

        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(25)
    public void user_login_Username_InvalidValue(){
        String TEST_CASE_NAME = "user_login_Username_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            
            LoginData invalidData = new LoginData("Ahmed Mater", "123456");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ahmed Mater",
                    LoginData.FIELDS.get("username"), RegExp.MESSAGES.get(RegExp.USERNAME));

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(26)
    public void user_login_Username_EmptyString(){
        String TEST_CASE_NAME = "user_login_Username_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            LoginData invalidData = new LoginData("", "123456");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, LoginData.FIELDS.get("username"));
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(27)
    public void user_login_Username_MinLength(){
        String TEST_CASE_NAME = "user_login_Username_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            String invalidValue = Util.generateString(2);
            LoginData invalidData = new LoginData(invalidValue, "123456");

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    LoginData.FIELDS.get("username"), 5, 50);

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(28)
    public void user_login_Username_MaxLength(){
        String TEST_CASE_NAME = "user_login_Username_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            String invalidValue = Util.generateString(55);
            LoginData invalidData =  new LoginData(invalidValue, "123456");

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    LoginData.FIELDS.get("username"), 5, 50);

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(29)
    public void user_login_Username_Required(){
        String TEST_CASE_NAME = "user_login_Username_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            LoginData invalidData = new LoginData(null, "123456");

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, LoginData.FIELDS.get("username"));

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

//    @Test @InSequence(26)
//    public void user_login_Password_InvalidValue(){
//        String TEST_CASE_NAME = "user_login_Password_InvalidValue";
//        try{
//            AppSession am.shared.session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            LoginData invalidData = this.loginData.clone();
//            invalidData.setPassword("Ah<med Mater");
//
//            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, INVALID_PASSWORD);
//            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(27)
//    public void user_login_Password_EmptyString(){
//        String TEST_CASE_NAME = "user_login_Password_EmptyString";
//        try{
//            AppSession am.shared.session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            LoginData invalidData = this.loginData.clone();
//            invalidData.setPassword("");
//
//            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, EMPTY_STR_PASSWORD);
//            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(28)
//    public void user_login_Password_MinLength(){
//        String TEST_CASE_NAME = "user_login_Password_MinLength";
//        try{
//            AppSession am.shared.session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            LoginData invalidData = this.loginData.clone();
//            invalidData.setPassword(Util.generateString(2));
//
//            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_PASSWORD);
//            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(29)
//    public void user_login_Password_MaxLength(){
//        String TEST_CASE_NAME = "user_login_Password_MaxLength";
//        try{
//            AppSession am.shared.session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            LoginData invalidData = this.loginData.clone();
//            invalidData.setPassword(Util.generateString(35));
//
//            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_PASSWORD);
//            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(30)
//    public void user_login_Password_Required(){
//        String TEST_CASE_NAME = "user_login_Password_Required";
//        try{
//            AppSession am.shared.session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//            LoginData invalidData = this.loginData.clone();
//            invalidData.setPassword(null);
//
//            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, REQUIRED_PASSWORD);
//            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }

    @Test @InSequence(37)
    public void user_login_Password_InvalidValue(){
        String TEST_CASE_NAME = "user_login_Password_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            LoginData invalidData = new LoginData("Ahmed_Mater", "Ah<med Mater");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ah<med Mater",
                    LoginData.FIELDS.get("password"), RegExp.MESSAGES.get(RegExp.PASSWORD));

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(38)
    public void user_login_Password_EmptyString(){
        String TEST_CASE_NAME = "user_login_Password_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            LoginData invalidData = new LoginData("Ahmed_Mater", "");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, LoginData.FIELDS.get("password"));
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(39)
    public void user_login_Password_MinLength(){
        String TEST_CASE_NAME = "user_login_Password_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            String invalidValue = Util.generateString(2);
            LoginData invalidData = new LoginData("Ahmed_Mater", invalidValue);
            invalidData.setPassword(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    LoginData.FIELDS.get("password"), 5, 30);

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(40)
    public void user_login_Password_MaxLength(){
        String TEST_CASE_NAME = "user_login_Password_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            String invalidValue = Util.generateString(40);
            LoginData invalidData = new LoginData("Ahmed_Mater", invalidValue);
            invalidData.setPassword(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    LoginData.FIELDS.get("password"), 5, 30);

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(41)
    public void user_login_Password_Required(){
        String TEST_CASE_NAME = "user_login_Password_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            LoginData invalidData = new LoginData("Ahmed_Mater", null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, LoginData.FIELDS.get("password"));

            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
    
    @Test @InSequence(31)
    public void endClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }
}
