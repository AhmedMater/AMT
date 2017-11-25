package amt.testCases.user;

import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.hibernate.model.user.UserIPDeActive;
import am.infrastructure.data.hibernate.model.user.UserIPFailure;
import am.infrastructure.data.hibernate.model.user.UserLoginLog;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AppConfigManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.enums.App_CC;
import am.shared.enums.EC;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Rest.USER;
import amt.common.enums.Scripts;
import amt.common.generic.DataGenerator;
import amt.common.generic.Repository;
import amt.common.generic.Util;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.*;

/**
 * Created by ahmed.motair on 11/22/2017.
 */
@RunWith(Arquillian.class)
public class UserLogin {
    @Inject private Repository repository;
    @Inject private AppConfigManager appConfigManager;
    @Inject private DataGenerator dataGenerator;
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

    @Test
    @InSequence(1)
    public void user_login_CorrectAuthentication_ActiveIP(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_ActiveIP";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData validData = this.loginData.clone();
            dataGenerator.login(validData);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", validData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertTrue("Success isn't correct", loginLog.getSuccess());
            Assert.assertNull("Error Code isn't null", loginLog.getErrorCode());
            Assert.assertNull("Error Message isn't null", loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(2)
    public void user_login_UsernameIsNotFound(){
        String TEST_CASE_NAME = "user_login_UsernameIsNotFound";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername("Ahmed_Ali");

            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN,
                    invalidData, MessageFormat.format(USER_IS_NOT_FOUND, invalidData.getUsername()));

            UserIPFailure userIPFailure = repository.getUserIPFailure(invalidData.getUsername());
            Assert.assertNotNull("IP isn't null", userIPFailure.getIp());
            Util.isEqualDates(new Date(), userIPFailure.getLoginDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(3)
    public void user_login_WrongPassword_NewIP(){
        String TEST_CASE_NAME = "user_login_WrongPassword_NewIP";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN,
                    invalidData, WRONG_PASSWORD);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0014.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", WRONG_PASSWORD, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(user.getUserID());
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(1), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(3)
    public void user_login_WrongPassword_ActiveIP_LessThanMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_LessThanMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Integer MAX_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
            int numOfTrails = MAX_TRAILS-1;

            for (int i = 0; i <numOfTrails; i++) {
                Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);
            }

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", numOfTrails, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0014.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", WRONG_PASSWORD, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(user.getUserID());
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(numOfTrails), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(4)
    public void user_login_WrongPassword_ActiveIP_EqualsMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_EqualsMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Integer MAX_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer LOGIN_DEACTIVATION_DURATION = appConfigManager.getConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);

            for (int i = 0; i <MAX_TRAILS-1; i++) {
                Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);
            }

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS.intValue(), loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0015.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", expectedErrorMsg, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(user.getUserID());
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", MAX_TRAILS, userIPDeActive.getTrailNum());
            Assert.assertFalse("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(5)
    public void user_login_WrongPassword_ActiveIP_GreaterThanMaxTrails(){
        String TEST_CASE_NAME = "user_login_WrongPassword_ActiveIP_LessThanMaxTrails";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Integer MAX_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer LOGIN_DEACTIVATION_DURATION = appConfigManager.getConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS + 1, LOGIN_DEACTIVATION_DURATION);
            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", numOfTrails, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0015.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct", expectedErrorMsg, loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(user.getUserID());
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(numOfTrails), userIPDeActive.getTrailNum());
            Assert.assertFalse("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(6)
    public void user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Integer LOGIN_DEACTIVATION_DURATION = 3;

            Integer MAX_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer OLD_LOGIN_DEACTIVATION_DURATION = appConfigManager.getConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);
            appConfigManager.updateConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, LOGIN_DEACTIVATION_DURATION.toString());

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            LoginData validData = this.loginData.clone();
            validData.setPassword("123456");

            Thread.sleep(65 * 1000);

            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, validData,
                    MessageFormat.format(WRONG_PASSWORD_LT_LOGIN_DEACTIVATION_DURATION, LOGIN_DEACTIVATION_DURATION-1));

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS + 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertFalse("Success isn't correct", loginLog.getSuccess());
            Assert.assertEquals("Error Code isn't correct", EC.AMT_0016.toString(), loginLog.getErrorCode());
            Assert.assertEquals("Error Message isn't correct",
                    MessageFormat.format(WRONG_PASSWORD_LT_LOGIN_DEACTIVATION_DURATION, LOGIN_DEACTIVATION_DURATION-1), loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            appConfigManager.updateConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, OLD_LOGIN_DEACTIVATION_DURATION.toString());
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(7)
    public void user_login_CorrectAuthentication_DeActiveIP_GreaterThanMaxDeactivateDuration(){
        String TEST_CASE_NAME = "user_login_CorrectAuthentication_DeActiveIP_LessThanMaxDeactivateDuration";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            Users user = dataGenerator.registerUser(userData.clone(), true);

            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("12advf34");

            Integer LOGIN_DEACTIVATION_DURATION = 1;

            Integer MAX_TRAILS = appConfigManager.getConfigValue(session, App_CC.MAX_LOGIN_TRAILS, Integer.class);
            Integer OLD_LOGIN_DEACTIVATION_DURATION = appConfigManager.getConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, Integer.class);
            appConfigManager.updateConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, LOGIN_DEACTIVATION_DURATION.toString());

            int numOfTrails = MAX_TRAILS+1;

            for (int i = 0; i <MAX_TRAILS-1; i++)
                Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, WRONG_PASSWORD);

            String expectedErrorMsg = MessageFormat.format(WRONG_PASSWORD_GT_MAX_TRAILS,
                    MAX_TRAILS, LOGIN_DEACTIVATION_DURATION);
            Util.callRestForStringError(USER.RESOURCE, USER.LOGIN, invalidData, expectedErrorMsg);

            LoginData validData = this.loginData.clone();
            validData.setPassword("123456");

            Thread.sleep(60 * 1000);
            dataGenerator.login(validData);

            List<UserLoginLog> loginLogList = repository.getUserLoginLog(user.getUserID());
            Assert.assertEquals("Number of LoginLog isn't correct", MAX_TRAILS + 1, loginLogList.size());
            UserLoginLog loginLog = loginLogList.get(loginLogList.size()-1);

            Assert.assertEquals("Username isn't correct", validData.getUsername(), loginLog.getUser().getUsername());
            Assert.assertTrue("Success isn't correct", loginLog.getSuccess());
            Assert.assertNull("Error Code isn't null", loginLog.getErrorCode());
            Assert.assertNull("Error Message isn't null", loginLog.getErrorMsg());
            Assert.assertNotNull("IP isn't null", loginLog.getIp());
            Util.isEqualDates(new Date(), loginLog.getLoginDate());

            UserIPDeActive userIPDeActive = repository.getUserIPDeActive(user.getUserID());
            Assert.assertEquals("Username isn't correct", invalidData.getUsername(), userIPDeActive.getUser().getUsername());
            Assert.assertNotNull("IP isn't null", userIPDeActive.getIp());
            Assert.assertEquals("Num Of Trails isn't correct", new Integer(1), userIPDeActive.getTrailNum());
            Assert.assertTrue("Active isn't correct", userIPDeActive.getActive());
            Util.isEqualDates(new Date(), userIPDeActive.getLastTrailDate());

            appConfigManager.updateConfigValue(session, App_CC.LOGIN_ACTIVATE_MINUTES, OLD_LOGIN_DEACTIVATION_DURATION.toString());
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(8)
    public void user_login_Username_AllowChar(){
        String TEST_CASE_NAME = "user_login_Username_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            
            UserRegisterData validUserData = userData.clone();
            validUserData.setUsername("AhmedMater");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setUsername("AhmedMater");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(9)
    public void user_login_Username_AllowUnderscore(){
        String TEST_CASE_NAME = "user_login_Username_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setUsername("Ahmed_Mater");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setUsername("Ahmed_Mater");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(10)
    public void user_login_Username_AllowPeriod(){
        String TEST_CASE_NAME = "user_login_Username_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setUsername("Ahmed.Mater");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setUsername("Ahmed.Mater");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(11)
    public void user_login_Username_AllowHyphen(){
        String TEST_CASE_NAME = "user_login_Username_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setUsername("Ahmed-Mater");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setUsername("Ahmed-Mater");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(12)
    public void user_login_Username_AllowNumber(){
        String TEST_CASE_NAME = "user_login_Username_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setUsername("AhmedMater123");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setUsername("AhmedMater123");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(13)
    public void user_login_Username_InvalidValue(){
        String TEST_CASE_NAME = "user_login_Username_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername("AhmedMat<>er123");
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, INVALID_USERNAME);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(14)
    public void user_login_Username_EmptyString(){
        String TEST_CASE_NAME = "user_login_Username_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername("");
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, EMPTY_STR_USERNAME);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(15)
    public void user_login_Username_MinLength(){
        String TEST_CASE_NAME = "user_login_Username_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername(Util.generateString(2));
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_USERNAME);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(16)
    public void user_login_Username_MaxLength(){
        String TEST_CASE_NAME = "user_login_Username_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername(Util.generateString(55));
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_USERNAME);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(17)
    public void user_login_Username_Required(){
        String TEST_CASE_NAME = "user_login_Username_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setUsername(null);
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, REQUIRED_USERNAME);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(18)
    public void user_login_Password_AllowChar(){
        String TEST_CASE_NAME = "user_login_Password_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("AhmedAli");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("AhmedAli");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(19)
    public void user_login_Password_AllowNumber(){
        String TEST_CASE_NAME = "user_login_Password_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("Ahmed123");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("Ahmed123");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(20)
    public void user_login_Password_AllowPeriod(){
        String TEST_CASE_NAME = "user_login_Password_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("Ahmed.Ali");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("Ahmed.Ali");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(21)
    public void user_login_Password_AllowHyphen(){
        String TEST_CASE_NAME = "user_login_Password_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("Ahmed-Ali");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("Ahmed-Ali");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(22)
    public void user_login_Password_AllowAmpersand(){
        String TEST_CASE_NAME = "user_login_Password_AllowAmpersand";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("Ahmed@Ali");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("Ahmed@Ali");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(23)
    public void user_login_Password_AllowUnderscore(){
        String TEST_CASE_NAME = "user_login_Password_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData validUserData = userData.clone();
            validUserData.setPassword("Ahmed_Ali");
            Users user = dataGenerator.registerUser(validUserData, true);

            LoginData validData = this.loginData.clone();
            validData.setPassword("Ahmed_Ali");
            dataGenerator.login(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(24)
    public void user_login_Password_InvalidValue(){
        String TEST_CASE_NAME = "user_login_Password_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("Ah<med Mater");
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, INVALID_PASSWORD);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(25)
    public void user_login_Password_EmptyString(){
        String TEST_CASE_NAME = "user_login_Password_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword("");
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, EMPTY_STR_PASSWORD);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(26)
    public void user_login_Password_MinLength(){
        String TEST_CASE_NAME = "user_login_Password_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword(Util.generateString(2));
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_PASSWORD);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(27)
    public void user_login_Password_MaxLength(){
        String TEST_CASE_NAME = "user_login_Password_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword(Util.generateString(35));
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, LENGTH_PASSWORD);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(28)
    public void user_login_Password_Required(){
        String TEST_CASE_NAME = "user_login_Password_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            LoginData invalidData = this.loginData.clone();
            invalidData.setPassword(null);
            
            FormValidation expected = new FormValidation(LOGIN_VALIDATION_ERROR, REQUIRED_PASSWORD);
            Util.callRestForFormValidation(USER.RESOURCE, USER.LOGIN, invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
}
