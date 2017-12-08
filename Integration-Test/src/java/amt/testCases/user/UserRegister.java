package amt.testCases.user;

import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AMSecurityManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.common.validation.RegExp;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
import amt.common.constants.Params;
import amt.common.constants.Rest;
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

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.*;
import static amt.common.constants.Rest.USER;

/**
 * Created by ahmed.motair on 11/18/2017.
 */
@RunWith(Arquillian.class)
public class UserRegister {
    @Inject private Repository repository;
    @Inject private DataGenerator dataGenerator;
    @Inject private AMSecurityManager securityManager;
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private UserRegisterData userData = new UserRegisterData("Ahmed", "Mater", 
            "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");

    private static final String CLASS = "UserRegister";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

    @Test @InSequence(1)
    public void startClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }

    @Test @InSequence(2)
    public void user_register_ValidRegisterData(){
        String TEST_CASE_NAME = "user_register_ValidRegisterData";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = new UserRegisterData();
            validData.setFirstName("Dr.Ali'M,Ah-Amr");
            validData.setLastName("Dr.Ali'M,Ah-Amr");
            validData.setUsername("amr.123-Mater_Ali_amr.123-Mater_Ali_amr.123-Mater5");
            validData.setPassword("ahmed@amr.ali-moh123@Mater_Ali");
            validData.setEmail("ali.ahmed-motair_Mohamed.user12@giza123-systems-co-ahmed.com");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(7)
    public void user_register_FirstName_InvalidValue(){
        String TEST_CASE_NAME = "user_register_FirstName_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setFirstName("Ahm/ed");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ahm/ed",
                    UserRegisterData.FIELDS.get("firstName"), RegExp.MESSAGES.get(RegExp.REAL_NAME));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);
            
            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(8)
    public void user_register_FirstName_EmptyString(){
        String TEST_CASE_NAME = "user_register_FirstName_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setFirstName("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, UserRegisterData.FIELDS.get("firstName"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(9)
    public void user_register_FirstName_MaxLength(){
        String TEST_CASE_NAME = "user_register_FirstName_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(20);
            invalidData.setFirstName(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("firstName"), 15);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(10)
    public void user_register_FirstName_Required(){
        String TEST_CASE_NAME = "user_register_FirstName_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setFirstName(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, UserRegisterData.FIELDS.get("firstName"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(16)
    public void user_register_LastName_InvalidValue(){
        String TEST_CASE_NAME = "user_register_LastName_";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setLastName("Ahm/ed");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ahm/ed",
                    UserRegisterData.FIELDS.get("lastName"), RegExp.MESSAGES.get(RegExp.REAL_NAME));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(17)
    public void user_register_LastName_EmptyString(){
        String TEST_CASE_NAME = "user_register_LastName_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setLastName("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, UserRegisterData.FIELDS.get("lastName"));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(18)
    public void user_register_LastName_MaxLength(){
        String TEST_CASE_NAME = "user_register_LastName_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(20);
            invalidData.setLastName(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("lastName"), 15);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(19)
    public void user_register_LastName_Required(){
        String TEST_CASE_NAME = "user_register_LastName_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setLastName(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, UserRegisterData.FIELDS.get("lastName"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(25)
    public void user_register_Username_InvalidValue(){
        String TEST_CASE_NAME = "user_register_Username_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername("Ahmed Mater");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ahmed Mater",
                    UserRegisterData.FIELDS.get("username"), RegExp.MESSAGES.get(RegExp.USERNAME));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(26)
    public void user_register_Username_EmptyString(){
        String TEST_CASE_NAME = "user_register_Username_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, UserRegisterData.FIELDS.get("username"));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(27)
    public void user_register_Username_MinLength(){
        String TEST_CASE_NAME = "user_register_Username_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(2);
            invalidData.setUsername(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("username"), 5, 50);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(28)
    public void user_register_Username_MaxLength(){
        String TEST_CASE_NAME = "user_register_Username_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(55);
            invalidData.setUsername(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("username"), 5, 50);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(29)
    public void user_register_Username_Required(){
        String TEST_CASE_NAME = "user_register_Username_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, UserRegisterData.FIELDS.get("username"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(30)
    public void user_register_Username_Duplicate(){
        String TEST_CASE_NAME = "user_register_Username_Duplicate";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            dataGenerator.registerUser(validData);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail("ahmedmotair@gmail.com");

            Util.postStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_USER, invalidData.getUsername()));
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(37)
    public void user_register_Password_InvalidValue(){
        String TEST_CASE_NAME = "user_register_Password_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setPassword("Ah<med Mater");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ah<med Mater",
                    UserRegisterData.FIELDS.get("password"), RegExp.MESSAGES.get(RegExp.PASSWORD));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(38)
    public void user_register_Password_EmptyString(){
        String TEST_CASE_NAME = "user_register_Password_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setPassword("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, UserRegisterData.FIELDS.get("password"));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(39)
    public void user_register_Password_MinLength(){
        String TEST_CASE_NAME = "user_register_Password_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(2);
            invalidData.setPassword(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("password"), 5, 30);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(40)
    public void user_register_Password_MaxLength(){
        String TEST_CASE_NAME = "user_register_Password_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(40);
            invalidData.setPassword(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("password"), 5, 30);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(41)
    public void user_register_Password_Required(){
        String TEST_CASE_NAME = "user_register_Password_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setPassword(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, UserRegisterData.FIELDS.get("password"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(47)
    public void user_register_Email_Invalid(){
        String TEST_CASE_NAME = "user_register_Email_Invalid";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail("Ahmed Mater");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, "Ahmed Mater",
                    UserRegisterData.FIELDS.get("email"), RegExp.MESSAGES.get(RegExp.EMAIL));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(48)
    public void user_register_Email_EmptyString(){
        String TEST_CASE_NAME = "user_register_Email_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, UserRegisterData.FIELDS.get("email"));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(49)
    public void user_register_Email_MinLength(){
        String TEST_CASE_NAME = "user_register_Email_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(2);
            invalidData.setEmail(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("email"), 10, 60);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(50)
    public void user_register_Email_MaxLength(){
        String TEST_CASE_NAME = "user_register_Email_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            String invalidValue = Util.generateString(105);
            invalidData.setEmail(invalidValue);

            String expectErrorMsg = MessageFormat.format(Error.FV.MIN_MAX_LENGTH, invalidValue,
                    UserRegisterData.FIELDS.get("email"), 10, 60);

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(51)
    public void user_register_Email_Required(){
        String TEST_CASE_NAME = "user_register_Email_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, UserRegisterData.FIELDS.get("email"));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, expectErrorMsg);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(52)
    public void user_register_Email_Duplicate(){
        String TEST_CASE_NAME = "user_register_Email_Duplicate";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            dataGenerator.registerUser(validData);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername("ahmed_Ali");

            Util.postStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_EMAIL, invalidData.getEmail()));
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(53)
    public void registerAdminUser_WithOwnerUser() throws Exception{
        String TEST_CASE_NAME = "registerAdminUser_WithOwnerUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);

            LoginData ownerLoginData = new LoginData(Params.OWNER_USERNAME, Params.OWNER_PASSWORD);

            UserRegisterData userData = new UserRegisterData();
            userData.setUsername("Admin_User");
            userData.setPassword("123456");
            userData.setFirstName("Admin");
            userData.setLastName("User");
            userData.setEmail("admin.user@amt.com");

            Response response = RestUtil.postSecured(Rest.USER.RESOURCE, Rest.USER.ADMIN_REGISTER, userData, ownerLoginData);
            Assert.assertEquals("Response Status is wrong", Response.Status.OK.getStatusCode(), response.getStatus());

            Users actual = repository.getUserByUsername(userData.getUsername());

            Assert.assertNotNull("User Object is null", actual);

            Assert.assertEquals("First name failed", userData.getFirstName(), actual.getFirstName());
            Assert.assertEquals("Last name failed", userData.getLastName(), actual.getLastName());
            Assert.assertEquals("Username failed", userData.getUsername(), actual.getUsername());
            Assert.assertEquals("Password failed", securityManager.dm5Hash(session, userData.getPassword()), actual.getPassword());
            Assert.assertEquals("Email failed", userData.getEmail(), actual.getEmail());
            Assert.assertEquals("Role failed", Roles.ADMIN.role(), actual.getRole().getRole());

            if(!Util.isEqualDates(new Date(), actual.getCreationDate()))
                Assert.fail("Creation Date failed");
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(54)
    public void registerAdminUse_WithAdminUser() throws Exception{
        String TEST_CASE_NAME = "registerAdminUse_WithAdminUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);

            LoginData adminLoginData = new LoginData(Params.ADMIN_USERNAME, Params.ADMIN_PASSWORD);

            UserRegisterData userData = new UserRegisterData();
            userData.setUsername("Admin_User1");
            userData.setPassword("123456");
            userData.setFirstName("Admin");
            userData.setLastName("User");
            userData.setEmail("admin.user@amt.com");

            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.ADMIN_REGISTER,
                    adminLoginData, userData, Error.NOT_AUTHORIZED);

            Users actual = repository.getUserByUsername(userData.getUsername());
            Assert.assertNull("User Object isn't null", actual);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(55)
    public void registerAdminUse_WithStudentUser() throws Exception{
        String TEST_CASE_NAME = "registerAdminUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);

            LoginData studentLoginData = new LoginData(Params.STUDENT_USERNAME, Params.STUDENT_PASSWORD);

            UserRegisterData userData = new UserRegisterData();
            userData.setUsername("Admin_User");
            userData.setPassword("123456");
            userData.setFirstName("Admin");
            userData.setLastName("User");
            userData.setEmail("admin.user@amt.com");

            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.ADMIN_REGISTER,
                    studentLoginData, userData, Error.NOT_AUTHORIZED);

            Users actual = repository.getUserByUsername(userData.getUsername());
            Assert.assertNull("User Object isn't null", actual);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
    
    @Test @InSequence(56)
    public void endClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }
}
