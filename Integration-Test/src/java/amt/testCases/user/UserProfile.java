package amt.testCases.user;

import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.UserProfileData;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.session.AppSession;
import am.shared.session.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
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
import java.util.ArrayList;
import java.util.List;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;

/**
 * Created by ahmed.motair on 11/24/2017.
 */
@RunWith(Arquillian.class)
public class UserProfile {
    @Inject
    private Repository repository;
    @Inject
    private DBManager dbManager;
    @Inject
    private DataGenerator dataGenerator;
    @Inject
    private ErrorHandler errorHandler;
    @Inject
    private InfoHandler infoHandler;

    private LoginData loginData = new LoginData("Ahmed_Mater", "123456");
    private UserRegisterData userData = new UserRegisterData("Ahmed", "Mater",
            "Ahmed_Mater", "123456", "ahmed.motair@gizasystems.com");

    private static final String CLASS = "UserLogin";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

    @Test @InSequence(1)
    public void startClearingAllDBTables() throws Exception {
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }

    @Test @InSequence(2)
    public void viewUserProfile_AsOwner() {
        String TEST_CASE_NAME = "viewUserProfile_AsOwner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData ownerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(ownerUserData);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());

            LoginData ownerLoginData = loginData.clone();
            ownerLoginData.setUsername(ownerUser.getUsername());

            Response response = RestUtil.getSecured(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, ownerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", ownerUser.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", ownerUser.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", ownerUser.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(3)
    public void viewUserProfile_AsAdmin() {
        String TEST_CASE_NAME = "viewUserProfile_AsAdmin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            LoginData adminLoginData = dataGenerator.getAdminLoginData().clone();

            UserRegisterData ownerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(ownerUserData);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());

            Response response = RestUtil.getSecured(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, adminLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", ownerUser.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", ownerUser.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", ownerUser.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List isn't correct", userProfileData.getRoleList());
            Assert.assertEquals("Role List isn't correct", 2, userProfileData.getRoleList().size());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(4)
    public void viewUserProfile_AsAnotherUser() {
        String TEST_CASE_NAME = "viewUserProfile_AsAnotherUser";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData viewerUserData = this.userData.clone();
            viewerUserData.setUsername("Ahmed_Mater2");
            viewerUserData.setEmail("ahmed.motair2@gizasystems.com");
            Users viewerUser = dataGenerator.registerUser(viewerUserData);

            UserRegisterData ownerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(ownerUserData);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());

            LoginData viewerLoginData = loginData.clone();
            viewerLoginData.setUsername(viewerUser.getUsername());

            Response response = RestUtil.getSecured(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, viewerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", ownerUser.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", ownerUser.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", ownerUser.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(5)
    public void viewUserProfile_WithoutLogin() {
        String TEST_CASE_NAME = "viewUserProfile_WithoutLogin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            List<String> pathParams = new ArrayList<>();
            pathParams.add("5");

            Response response = RestUtil.get(Rest.USER.RESOURCE, Rest.USER.GET_PROFILE.PATH, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(6)
    public void upgradeRole_To_Tutor_AsAdmin() {
        String TEST_CASE_NAME = "upgradeRole_To_Tutor_AsAdmin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData ownerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(ownerUserData);

            LoginData adminLoginData = dataGenerator.getAdminLoginData().clone();

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());
            Response response = RestUtil.postSecured(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, Roles.TUTOR.role(), pathParams, adminLoginData);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            ownerUser = dbManager.find(session, Users.class, ownerUser.getUserID(), false);
            Assert.assertEquals("Role hasn't been changed", Roles.TUTOR.role(), ownerUser.getRole().getRole());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(7)
    public void upgradeRole_To_Admin_AsAdmin() {
        String TEST_CASE_NAME = "upgradeRole_To_Admin_AsAdmin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData OwnerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(OwnerUserData);

            LoginData adminLoginData = dataGenerator.getAdminLoginData().clone();

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());

            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, pathParams,
                    Roles.ADMIN.role(), Error.USER.CANT_UPGRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(8)
    public void upgradeRole_To_InvalidRole_AsAdmin(){
        String TEST_CASE_NAME = "upgradeRole_To_InvalidRole_AsAdmin";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData ownerUserData = this.userData.clone();
            Users ownerUser = dataGenerator.registerUser(ownerUserData);

            LoginData adminLoginData = dataGenerator.getAdminLoginData().clone();

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ownerUser.getUserID().toString());

            String expectedError = MessageFormat.format(Error.USER.INVALID_ROLE, "IR");
            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, pathParams,
                    "IR", expectedError);

//            ChangeRoleData changeRoleData = new ChangeRoleData(ownerUser.getUserID(), ADMIN_USER_ID, "IR");
//            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData,
//                    changeRoleData, expectedError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(9)
    public void upgradeRole_AsStudent(){
        String TEST_CASE_NAME = "upgradeRole_AsStudent";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData studentUserData = this.userData.clone();
            Users studentUser = dataGenerator.registerUser(studentUserData);

            UserRegisterData changerUserData = this.userData.clone();
            changerUserData.setUsername("Amr_Mater");
            changerUserData.setEmail("amr.mater@gmail.com");
            Users changerUser = dataGenerator.registerUser(changerUserData);

            LoginData changerUserLoginData = loginData.clone();
            changerUserLoginData.setUsername(changerUser.getUsername());

            List<String> pathParams = new ArrayList<>();
            pathParams.add(studentUser.getUserID().toString());

            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changerUserLoginData, pathParams,
                    Roles.TUTOR.role(), Error.NOT_AUTHORIZED);

//            ChangeRoleData changeRoleData = new ChangeRoleData(studentUser.getUserID(), changerUser.getUserID(), Roles.TUTOR.role());
//            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE,
//                    changerUserLoginData, changeRoleData, Error.NOT_AUTHORIZED);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(10)
    public void upgradeRole_AsTutor(){
        String TEST_CASE_NAME = "upgradeRole_AsTutor";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);

            UserRegisterData tutorUserData = this.userData.clone();
            Users tutorUser = dataGenerator.registerUser(tutorUserData);

            dataGenerator.upgradeUserRole_ToTutor(tutorUser.getUserID());

            UserRegisterData studentUserData = this.userData.clone();
            studentUserData.setUsername("Amr_Mater");
            studentUserData.setEmail("amr.mater@gmail.com");
            Users studentUser = dataGenerator.registerUser(studentUserData);

            LoginData tutorLoginData = loginData.clone();
            tutorLoginData.setUsername(tutorUserData.getUsername());

            List<String> pathParams = new ArrayList<>();
            pathParams.add(studentUser.getUserID().toString());

            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, tutorLoginData, pathParams,
                    Roles.TUTOR.role(), Error.NOT_AUTHORIZED);

//            changeRoleData = new ChangeRoleData(studentUser.getUserID(), tutorUser.getUserID(), Roles.TUTOR.role());
//            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, tutorLoginData, changeRoleData, Error.NOT_AUTHORIZED);
//
//            List<String> pathParams = new ArrayList<>();
//            pathParams.add(ADMIN_USER_ID.toString());
//
//            Util.postSecuredStringError(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, changerUserLoginData, pathParams,
//                    Roles.TUTOR.role(), Error.NOT_AUTHORIZED);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

//    @Test @InSequence(11)
//    public void changeDataRole_OwnerID_Required(){
//        String TEST_CASE_NAME = "changeDataRole_OwnerID_Required";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            List<String> pathParams = new ArrayList<>();
//            pathParams.add(null);
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_OWNER_ID);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, pathParams, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }

//
//    @Test @InSequence(12)
//    public void changeDataRole_OwnerID_Invalid(){
//        String TEST_CASE_NAME = "changeDataRole_OwnerID_Invalid";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(-1, 25, Roles.TUTOR.role());
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, INVALID_OWNER_ID);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(13)
//    public void changeDataRole_ViewerID_Required(){
//        String TEST_CASE_NAME = "changeDataRole_ViewerID_Required";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(12, null, Roles.TUTOR.role());
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_VIEWER_ID);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(14)
//    public void changeDataRole_ViewerID_Invalid(){
//        String TEST_CASE_NAME = "changeDataRole_ViewerID_Invalid";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(24, -1, Roles.TUTOR.role());
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, INVALID_VIEWER_ID);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(15)
//    public void changeDataRole_Role_Required(){
//        String TEST_CASE_NAME = "changeDataRole_Role_Required";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(12, 23, null);
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, REQUIRED_ROLE);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(16)
//    public void changeDataRole_Role_EmptyStr(){
//        String TEST_CASE_NAME = "changeDataRole_Role_EmptyStr";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(24, 24, "");
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, EMPTY_STR_ROLE);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }
//
//    @Test @InSequence(17)
//    public void changeDataRole_Role_Invalid(){
//        String TEST_CASE_NAME = "changeDataRole_Role_Invalid";
//        try{
//            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);
//
//            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
//            repository.executeScript(Scripts.ROLE_LOOKUP);
//
//            LoginData adminLoginData = dataGenerator.getAdminLoginData();
//
//            ChangeRoleData changeRoleData = new ChangeRoleData(24, 23, "asd");
//
//            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, LENGTH_ROLE);
//            Util.postSecuredFormValidation(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, adminLoginData, changeRoleData, expected);
//        }catch (Exception ex){
//            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
//        }
//    }

}
