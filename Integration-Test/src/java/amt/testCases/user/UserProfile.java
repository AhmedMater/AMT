package amt.testCases.user;

import am.infrastructure.data.dto.user.ChangeRoleData;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.UserProfileData;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.common.validation.FormValidation;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
import amt.common.constants.Rest;
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
import javax.ws.rs.core.Response;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.*;

/**
 * Created by ahmed.motair on 11/24/2017.
 */
@RunWith(Arquillian.class)
public class UserProfile {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
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
    public void viewUserProfile_AsOwner(){
        String TEST_CASE_NAME = "viewUserProfile_AsOwner";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData = this.userData.clone();
            Users owner = dataGenerator.registerUser(userData, true);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(owner.getUserID().toString());

            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(Rest.USER.GET_PROFILE.QP_VIEWER_ID, owner.getUserID().toString());

            LoginData validLoginData = loginData.clone();
            Response response = Util.restGETClient(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, queryParams, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", owner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", owner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", owner.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(2)
    public void upgradeRole_To_Tutor_AsAdmin(){
        String TEST_CASE_NAME = "upgradeRole_To_Tutor_AsAdmin";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData ownerData = this.userData.clone();
            Users owner = dataGenerator.registerUser(ownerData, true);

            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            Users adminUser = dbManager.find(session, Users.class, 1, false);

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername("Admin_User");

            ChangeRoleData changeRoleData = new ChangeRoleData(owner.getUserID(), adminUser.getUserID(), Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            owner = dbManager.find(session, Users.class, owner.getUserID(), false);
            Assert.assertEquals("Role hasn't been changed", Roles.TUTOR.role(), owner.getRole().getRole());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(3)
    public void upgradeRole_To_Admin_AsAdmin(){
        String TEST_CASE_NAME = "upgradeRole_To_Admin_AsAdmin";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData = this.userData.clone();
            Users owner = dataGenerator.registerUser(userData, true);

            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            Users adminUser = dbManager.find(session, Users.class, 1, false);

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername("Admin_User");

//            Response response = dataGenerator.upgradeUserRole(owner.getUserID(), validLoginData, Roles.ADMIN.role());
            ChangeRoleData changeRoleData = new ChangeRoleData(owner.getUserID(), adminUser.getUserID(), Roles.ADMIN.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            String actualError = response.readEntity(String.class);
            Assert.assertEquals("Error returned isn't correct", Error.USER.CANT_UPGRADE_TO_ADMIN, actualError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(4)
    public void upgradeRole_To_InvalidRole_AsAdmin(){
        String TEST_CASE_NAME = "upgradeRole_To_InvalidRole_AsAdmin";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData = this.userData.clone();
            Users owner = dataGenerator.registerUser(userData, true);

            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            Users adminUser = dbManager.find(session, Users.class, 1, false);

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername(adminUser.getUsername());

            ChangeRoleData changeRoleData = new ChangeRoleData(owner.getUserID(), adminUser.getUserID(), "IR");
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            String actualError = response.readEntity(String.class);
            String expectedError = MessageFormat.format(Error.USER.INVALID_ROLE, "IR");
            Assert.assertEquals("Error returned isn't correct", expectedError, actualError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(5)
    public void upgradeRole_AsStudent(){
        String TEST_CASE_NAME = "upgradeRole_AsStudent";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData1 = this.userData.clone();
            Users owner1 = dataGenerator.registerUser(userData1, true);

            UserRegisterData userData2 = this.userData.clone();
            userData2.setUsername("Amr_Mater");
            userData2.setEmail("amr.mater@gmail.com");
            Users owner2 = dataGenerator.registerUser(userData2, false);

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername(owner2.getUsername());

            ChangeRoleData changeRoleData = new ChangeRoleData(owner1.getUserID(), owner2.getUserID(), Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());

            String actualError = response.readEntity(String.class);
            Assert.assertEquals("Error returned isn't correct", Error.NOT_AUTHORIZED, actualError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(6)
    public void upgradeRole_AsTutor(){
        String TEST_CASE_NAME = "upgradeRole_AsTutor";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData1 = this.userData.clone();
            Users owner1 = dataGenerator.registerUser(userData1, true);

            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            Users adminUser = dbManager.find(session, Users.class, 1, false);

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername(adminUser.getUsername());

            ChangeRoleData changeRoleData = new ChangeRoleData(owner1.getUserID(), adminUser.getUserID(), Roles.TUTOR.role());
            Response response1 = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response1.getStatus());

            owner1 = dbManager.find(session, Users.class, owner1.getUserID(), false);
            Assert.assertEquals("Role hasn't been changed", Roles.TUTOR.role(), owner1.getRole().getRole());

            UserRegisterData userData2 = this.userData.clone();
            userData2.setUsername("Amr_Mater");
            userData2.setEmail("amr.mater@gmail.com");
            Users owner2 = dataGenerator.registerUser(userData2, false);

            validLoginData.setUsername(userData1.getUsername());

            changeRoleData = new ChangeRoleData(owner2.getUserID(), owner2.getUserID(), Roles.TUTOR.role());
            Response response2 = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changeRoleData, validLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.FORBIDDEN.getStatusCode(), response2.getStatus());

            String actualError = response2.readEntity(String.class);
            Assert.assertEquals("Error returned isn't correct", Error.NOT_AUTHORIZED, actualError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(7)
    public void viewUserProfile_AsAdmin(){
        String TEST_CASE_NAME = "viewUserProfile_AsAdmin";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData = this.userData.clone();
            Users owner = dataGenerator.registerUser(userData, true);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(owner.getUserID().toString());

            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            Users adminUser = dbManager.find(session, Users.class, 1, false);

            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(Rest.USER.GET_PROFILE.QP_VIEWER_ID, adminUser.getUserID().toString());

            LoginData validLoginData = loginData.clone();
            validLoginData.setUsername("Admin_User");

            Response response = Util.restGETClient(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, queryParams, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", owner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", owner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", owner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List isn't correct", userProfileData.getRoleList());
            Assert.assertEquals("Role List isn't correct", 2, userProfileData.getRoleList().size());
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(8)
    public void viewUserProfile_AsAnotherUser(){
        String TEST_CASE_NAME = "viewUserProfile_AsAnotherUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
            repository.executeScript(Scripts.CLEARING_ALL_TABLES);

            UserRegisterData userData = this.userData.clone();
            Users owner = dataGenerator.registerUser(userData, true);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(owner.getUserID().toString());

            LoginData validLoginData = loginData.clone();
            Response response = Util.restGETClient(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", owner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", owner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", owner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());

        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(9)
    public void changeDataRole_OwnerID_Required(){
        String TEST_CASE_NAME = "changeDataRole_OwnerID_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(null, 25, Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_OWNER_ID);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(10)
    public void changeDataRole_OwnerID_Invalid(){
        String TEST_CASE_NAME = "changeDataRole_OwnerID_Invalid";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(-1, 25, Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, INVALID_OWNER_ID);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(11)
    public void changeDataRole_ViewerID_Required(){
        String TEST_CASE_NAME = "changeDataRole_ViewerID_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(12, null, Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_VIEWER_ID);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(12)
    public void changeDataRole_ViewerID_Invalid(){
        String TEST_CASE_NAME = "changeDataRole_ViewerID_Invalid";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(24, -1, Roles.TUTOR.role());
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, INVALID_VIEWER_ID);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(13)
    public void changeDataRole_Role_Required(){
        String TEST_CASE_NAME = "changeDataRole_Role_Required";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(12, 23, null);
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_ROLE);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(14)
    public void changeDataRole_Role_EmptyStr(){
        String TEST_CASE_NAME = "changeDataRole_Role_EmptyStr";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(24, 24, "");
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, EMPTY_STR_ROLE);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test
    @InSequence(15)
    public void changeDataRole_Role_Invalid(){
        String TEST_CASE_NAME = "changeDataRole_Role_Invalid";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_USER_TABLE);
            LoginData adminLoginData = dataGenerator.getAdminLoginData();

            ChangeRoleData changeRoleData = new ChangeRoleData(24, 23, "asd");
            Response response = Util.restPOSTSecuredClient(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
                    changeRoleData, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());

            FormValidation actual = response.readEntity(FormValidation.class);
            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, LENGTH_ROLE);

            Util.validateInvalidFormField(actual, expected);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

}
