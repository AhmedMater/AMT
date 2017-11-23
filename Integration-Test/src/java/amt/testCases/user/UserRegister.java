package amt.testCases.user;

import am.infrastructure.data.dto.UserRegisterData;
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
    public void beforeClass(){
        try {
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
        } catch (Exception ex){
            Assert.fail(MessageFormat.format(SET_UP, ex.getMessage()));
        }
    }

    private void callRestForFormValidation(UserRegisterData data, FormValidation expected) throws Exception{
        Util.callRestForFormValidation(USER.RESOURCE, USER.REGISTER, data, expected);

        Users user = repository.getUserByUsername(data.getUsername());
        Assert.assertNull("User is found in Database", user);
    }

    private void callRestOk(AppSession session, UserRegisterData expected) throws Exception{
        dataGenerator.registerUser(expected);
        repository.executeScript(Scripts.CLEARING_USER_TABLE);
    }

    @Test
    public void user_register_FirstName_AllowChar(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setFirstName("Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setFirstName("Ali-Amr");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_AllowComma(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowComma";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setFirstName("Ahmed,Amr");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setFirstName("Dr.Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_AllowApostrophe(){
        String TEST_CASE_NAME = "user_register_FirstName_AllowApostrophe";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setFirstName("Dr'Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_InvalidValue(){
        String TEST_CASE_NAME = "user_register_FirstName_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setFirstName("Ahm/ed");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_EmptyString(){
        String TEST_CASE_NAME = "user_register_FirstName_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setFirstName("");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_MaxLength(){
        String TEST_CASE_NAME = "user_register_FirstName_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setFirstName(Util.generateString(20));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_FirstName_Required(){
        String TEST_CASE_NAME = "user_register_FirstName_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setFirstName(null);
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_FIRST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_AllowChar(){
        String TEST_CASE_NAME = "user_register_LastName_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setLastName("Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_LastName_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setLastName("Ali-Amr");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_AllowComma(){
        String TEST_CASE_NAME = "user_register_LastName_AllowComma";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setLastName("Ahmed,Amr");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_LastName_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setLastName("Dr.Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_AllowApostrophe(){
        String TEST_CASE_NAME = "user_register_LastName_AllowApostrophe";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setLastName("Dr'Ahmed");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_InvalidValue(){
        String TEST_CASE_NAME = "user_register_LastName_";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setLastName("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_LAST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_EmptyString(){
        String TEST_CASE_NAME = "user_register_LastName_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setLastName("");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_LAST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_MaxLength(){
        String TEST_CASE_NAME = "user_register_LastName_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setLastName(Util.generateString(55));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_LAST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_LastName_Required(){
        String TEST_CASE_NAME = "user_register_LastName_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setLastName(null);
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_LAST_NAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_AllowChar(){
        String TEST_CASE_NAME = "user_register_Username_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setUsername("AhmedMater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_AllowUnderscore(){
        String TEST_CASE_NAME = "user_register_Username_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setUsername("Ahmed_Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Username_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setUsername("Ahmed.Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Username_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setUsername("Ahmed-Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Username_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setUsername("Ahmed123");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_InvalidValue(){
        String TEST_CASE_NAME = "user_register_Username_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setUsername("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_EmptyString(){
        String TEST_CASE_NAME = "user_register_Username_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setUsername("");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_MinLength(){
        String TEST_CASE_NAME = "user_register_Username_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setUsername(Util.generateString(2));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_MaxLength(){
        String TEST_CASE_NAME = "user_register_Username_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setUsername(Util.generateString(55));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_Required(){
        String TEST_CASE_NAME = "user_register_Username_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setUsername(null);
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_USERNAME);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Username_Duplicate(){
        String TEST_CASE_NAME = "user_register_Username_Duplicate";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            UserRegisterData validData = this.userData.clone();
            dataGenerator.registerUser(validData);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setEmail("ahmedmotair@gmail.com");

            Util.callRestForStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_USER, invalidData.getUsername()));
            repository.executeScript(Scripts.CLEARING_USER_TABLE);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowChar(){
        String TEST_CASE_NAME = "user_register_Password_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            // Allow Chars
            validData.setPassword("AhmedMater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Password_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setPassword("Ahmed123");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Password_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setPassword("Ahmed.Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Password_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setPassword("Ahmed-Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowAmpersand(){
        String TEST_CASE_NAME = "user_register_Password_AllowAmpersand";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setPassword("Ahmed@Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_AllowUnderscore(){
        String TEST_CASE_NAME = "user_register_Password_AllowUnderscore";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setPassword("Ahmed_Mater");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_InvalidValue(){
        String TEST_CASE_NAME = "user_register_Password_InvalidValue";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setPassword("Ah<med Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_EmptyString(){
        String TEST_CASE_NAME = "user_register_Password_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setPassword("");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_MinLength(){
        String TEST_CASE_NAME = "user_register_Password_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setPassword(Util.generateString(2));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_MaxLength(){
        String TEST_CASE_NAME = "user_register_Password_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setPassword(Util.generateString(35));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Password_Required(){
        String TEST_CASE_NAME = "user_register_Password_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setPassword(null);
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_PASSWORD);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_AllowChar(){
        String TEST_CASE_NAME = "user_register_Email_AllowChar";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setEmail("ahmedmotair@gmail.com");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_AllowNumber(){
        String TEST_CASE_NAME = "user_register_Email_AllowNumber";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setEmail("ahmedmotair123@gmail.com");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_AllowPeriod(){
        String TEST_CASE_NAME = "user_register_Email_AllowPeriod";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setEmail("ahmed.motair@gizasystem.com");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_AllowHyphen(){
        String TEST_CASE_NAME = "user_register_Email_AllowHyphen";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setEmail("ahmed-motair@gizasystems.com");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_AllowHyphenInDomain(){
        String TEST_CASE_NAME = "user_register_Email_AllowHyphenInDomain";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData validData = this.userData.clone();

            validData.setEmail("ahmedmotair@giza-systems.com");
            callRestOk(session, validData);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_Invalid(){
        String TEST_CASE_NAME = "user_register_Email_Invalid";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setEmail("Ahmed Mater");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, INVALID_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_EmptyString(){
        String TEST_CASE_NAME = "user_register_Email_EmptyString";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setEmail("");
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, EMPTY_STR_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_MinLength(){
        String TEST_CASE_NAME = "user_register_Email_MinLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setEmail(Util.generateString(2));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_MaxLength(){
        String TEST_CASE_NAME = "user_register_Email_MaxLength";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setEmail(Util.generateString(105));
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, LENGTH_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_Required(){
        String TEST_CASE_NAME = "user_register_Email_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            UserRegisterData invalidData = this.userData.clone();

            invalidData.setEmail(null);
            FormValidation expected = new FormValidation(REGISTER_VALIDATION_ERROR, REQUIRED_EMAIL);
            callRestForFormValidation(invalidData, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    public void user_register_Email_Duplicate(){
        String TEST_CASE_NAME = "user_register_Email_Duplicate";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            UserRegisterData validData = this.userData.clone();
            dataGenerator.registerUser(validData);

            UserRegisterData invalidData = this.userData.clone();
            invalidData.setUsername("ahmed_Ali");

            Util.callRestForStringError(USER.RESOURCE, USER.REGISTER,
                    invalidData, MessageFormat.format(Error.USER.DUPLICATE_EMAIL, invalidData.getEmail()));

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }
}
