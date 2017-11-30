package amt.testCases.user;

import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AMSecurityManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
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
    public void user_register_FirstName_AllowChar(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            
            UserRegisterData validData = this.userData.clone();
            validData.setFirstName("Ahmed");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(3)
    public void user_register_FirstName_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            
            UserRegisterData validData = this.userData.clone();
            validData.setFirstName("Ali-Amr");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(4)
    public void user_register_FirstName_AllowComma(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowComma";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setFirstName("Ahmed,Amr");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(5)
    public void user_register_FirstName_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setFirstName("Dr.Ahmed");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(6)
    public void user_register_FirstName_AllowApostrophe(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowApostrophe";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setFirstName("Dr'Ahmed");

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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_FIRST_NAME);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_FIRST_NAME);
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
            invalidData.setFirstName(Util.generateString(20));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_FIRST_NAME);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_FIRST_NAME);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(11)
    public void user_register_LastName_AllowChar(){
        String TEST_CASE_NAME = "user_register_LastName_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setLastName("Ahmed");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(12)
    public void user_register_LastName_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_LastName_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setLastName("Ali-Amr");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(13)
    public void user_register_LastName_AllowComma(){
        String TEST_CASE_NAME = "user_register_LastName_AllowComma";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setLastName("Ahmed,Amr");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(14)
    public void user_register_LastName_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_LastName_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setLastName("Dr.Ahmed");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(15)
    public void user_register_LastName_AllowApostrophe(){
        String TEST_CASE_NAME = "user_register_LastName_AllowApostrophe";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setLastName("Dr'Ahmed");

            dataGenerator.registerUser(validData);
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
            invalidData.setLastName("Ahmed Mater");

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_LAST_NAME);            
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_LAST_NAME);
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
            invalidData.setLastName(Util.generateString(55));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_LAST_NAME);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_LAST_NAME);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(20)
    public void user_register_Username_AllowChar(){
        String TEST_CASE_NAME = "user_register_Username_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setUsername("AhmedMater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(21)
    public void user_register_Username_AllowUnderscore(){
        String TEST_CASE_NAME = "user_register_Username_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setUsername("Ahmed_Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(22)
    public void user_register_Username_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Username_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setUsername("Ahmed.Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(23)
    public void user_register_Username_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Username_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setUsername("Ahmed-Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(24)
    public void user_register_Username_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Username_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setUsername("Ahmed123");

            dataGenerator.registerUser(validData);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_USERNAME);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_USERNAME);
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
            invalidData.setUsername(Util.generateString(2));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
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
            invalidData.setUsername(Util.generateString(55));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_USERNAME);
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

    @Test @InSequence(31)
    public void user_register_Password_AllowChar(){
        String TEST_CASE_NAME = "user_register_Password_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("AhmedMater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(32)
    public void user_register_Password_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Password_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("Ahmed123");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(33)
    public void user_register_Password_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Password_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("Ahmed.Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(34)
    public void user_register_Password_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Password_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("Ahmed-Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(35)
    public void user_register_Password_AllowAmpersand(){
        String TEST_CASE_NAME = "user_register_Password_AllowAmpersand";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("Ahmed@Mater");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(36)
    public void user_register_Password_AllowUnderscore(){
        String TEST_CASE_NAME = "user_register_Password_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setPassword("Ahmed_Mater");

            dataGenerator.registerUser(validData);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_PASSWORD);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_PASSWORD);
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
            invalidData.setPassword(Util.generateString(2));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
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
            invalidData.setPassword(Util.generateString(35));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_PASSWORD);
            Util.postFormValidation(USER.RESOURCE, USER.REGISTER, invalidData, expected);

            Users user = repository.getUserByUsername(invalidData.getUsername());
            Assert.assertNull("User is found in Database", user);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(42)
    public void user_register_Email_AllowChar(){
        String TEST_CASE_NAME = "user_register_Email_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setEmail("ahmedmotair@gmail.com");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(43)
    public void user_register_Email_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Email_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setEmail("ahmedmotair123@gmail.com");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(44)
    public void user_register_Email_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Email_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setEmail("ahmed.motair@gizasystem.com");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(45)
    public void user_register_Email_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Email_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setEmail("ahmed-motair@gizasystems.com");

            dataGenerator.registerUser(validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(46)
    public void user_register_Email_AllowHyphenInDomain(){
        String TEST_CASE_NAME = "user_register_Email_AllowHyphenInDomain";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData validData = this.userData.clone();
            validData.setEmail("ahmedmotair@giza-systems.com");

            dataGenerator.registerUser(validData);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_EMAIL);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_EMAIL);
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
            invalidData.setEmail(Util.generateString(2));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
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
            invalidData.setEmail(Util.generateString(105));

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
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

            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_EMAIL);
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
    public void endClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }
}
