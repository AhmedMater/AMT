package amt.testCases.user;

import am.infrastructure.data.dto.user.ChangeRoleData;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.UserProfileData;
import am.main.api.db.DBManager;
import am.main.api.validation.FormValidation;
import am.main.common.RegExp;
import am.main.session.AppSession;
import am.shared.enums.Phase;
import amt.common.DeploymentManger;
import amt.common.constants.Error;
import amt.common.constants.Params;
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
import java.util.ArrayList;
import java.util.List;

import static am.shared.enums.Interface.ARQUILLIAN;
import static am.shared.enums.Source.INTEGRATION_TEST;
import static amt.common.constants.Error.TEST_CASE;
import static amt.common.constants.Error.USER.CHANGE_ROLE_VALIDATION_ERROR;

/**
 * Created by ahmed.motair on 11/24/2017.
 */
@RunWith(Arquillian.class)
public class UserProfile {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private DataGenerator dataGenerator;

    private static final Integer STD_USER_ID = 5;
    private static final Integer TUTOR_USER_ID = 8;
    private static final Integer ADMIN_USER_ID = 6;
    private static final Integer OWNER_USER_ID = 7;
    private static final Integer TUTOR_HAS_COURSES_USER_ID = 9;
    private static final String CLASS = "UserLogin";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST);

    @Deployment
    public static WebArchive createDeployment() {
        return DeploymentManger.createDeployment(Scripts.getAllScripts());
    }

    @Test @InSequence(1)
    public void startClearingAllDBTables() throws Exception {
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }

    @Test @InSequence(2)
    public void viewUserProfile_Student_By_Owner() {
        String TEST_CASE_NAME = "viewUserProfile_Student_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, STD_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH,
                    Params.ownerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List is null", userProfileData.getRoleList());
            Assert.assertEquals("Role List has more than expected roles", 2, userProfileData.getRoleList().size());

            for (Role role : userProfileData.getRoleList())
                Assert.assertFalse("Role List has Admin Role", role.isAdmin());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(3)
    public void viewUserProfile_Student_By_Admin() {
        String TEST_CASE_NAME = "viewUserProfile_Student_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, STD_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.adminLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List is null", userProfileData.getRoleList());
            Assert.assertEquals("Role List has more than expected roles", 2, userProfileData.getRoleList().size());

            for (Role role : userProfileData.getRoleList())
                Assert.assertFalse("Role List has Admin Role", role.isAdmin());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(4)
    public void viewUserProfile_Student_By_Student() {
        String TEST_CASE_NAME = "viewUserProfile_Student_By_Student";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, STD_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.studentLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(5)
    public void viewUserProfile_Student_By_ProfileOwner() {
        String TEST_CASE_NAME = "viewUserProfile_Student_By_ProfileOwner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, STD_USER_ID, false);

            LoginData profileOwnerLoginData = new LoginData("Test_User_5","123456");

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, profileOwnerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(6)
    public void upgradeRole_From_Student_To_Admin_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Student_To_Admin_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(7)
    public void upgradeRole_From_Student_To_Admin_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Student_To_Admin_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(8)
    public void upgradeRole_From_Student_To_Owner_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Student_To_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(9)
    public void upgradeRole_From_Student_To_Owner_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Student_To_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(10)
    public void upgradeRole_From_Tutor_To_Admin_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Admin_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(11)
    public void upgradeRole_From_Tutor_To_Admin_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Admin_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(12)
    public void upgradeRole_From_Tutor_To_Owner_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(13)
    public void upgradeRole_From_Tutor_To_Owner_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_ASSIGN_ADMIN_ROLES);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(14)
    public void upgradeRole_From_Owner_To_Admin_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Admin_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(15)
    public void upgradeRole_From_Owner_To_Admin_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Admin_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.ADMIN.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(16)
    public void upgradeRole_From_Owner_To_Student_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(17)
    public void upgradeRole_From_Owner_To_Student_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(18)
    public void upgradeRole_From_Owner_To_Tutor_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Tutor_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(19)
    public void upgradeRole_From_Owner_To_Tutor_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Owner_To_Tutor_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(20)
    public void upgradeRole_From_Admin_To_Owner_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(21)
    public void upgradeRole_From_Admin_To_Owner_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.OWNER.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(22)
    public void upgradeRole_From_Admin_To_Student_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(23)
    public void upgradeRole_From_Admin_To_Student_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(24)
    public void upgradeRole_From_Admin_To_Tutor_By_Admin() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Tutor_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(25)
    public void upgradeRole_From_Admin_To_Tutor_By_Owner() {
        String TEST_CASE_NAME = "upgradeRole_From_Admin_To_Tutor_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_UP_DOWN_GRADE_TO_ADMIN);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(26)
    public void upgradeRole_From_Tutor_To_Student_By_Admin_HasCourses() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Student_By_Admin_HasCourses";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.TUTOR_HAS_COURSES);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_HAS_COURSES_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, Error.USER.CANT_CHANGE_TUTOR_ROLE);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(27)
    public void upgradeRole_From_Tutor_To_Student_By_Owner_HasCourses() {
        String TEST_CASE_NAME = "upgradeRole_From_Tutor_To_Student_By_Owner_HasCourses";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.CONTENT_STATUS_LOOKUP);
            repository.executeScript(Scripts.COURSE_TYPE_LOOKUP);
            repository.executeScript(Scripts.COURSE_LEVEL_LOOKUP);
            repository.executeScript(Scripts.TUTOR_HAS_COURSES);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_HAS_COURSES_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.STUDENT.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.ownerLoginData, pathParams,
                    payload, Error.USER.CANT_CHANGE_TUTOR_ROLE);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(28)
    public void upgradeRole_ByStudentUser(){
        String TEST_CASE_NAME = "upgradeRole_ByStudentUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.studentLoginData, pathParams,
                    payload, Error.NOT_AUTHORIZED);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(29)
    public void upgradeRole_ByTutorUser(){
        String TEST_CASE_NAME = "upgradeRole_ByTutorUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(Roles.TUTOR.role());

            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.tutorLoginData, pathParams,
                    payload, Error.NOT_AUTHORIZED);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(30)
    public void upgradeRole_To_InvalidRole_By_AdminUser(){
        String TEST_CASE_NAME = "upgradeRole_To_InvalidRole_By_AdminUser";
        try{
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData("IR");

            String expectedError = MessageFormat.format(Error.LOOKUP_NOT_FOUND, Role.class.getSimpleName(), payload.getNewRole());
            Util.postSecuredStringError(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData, pathParams,
                    payload, expectedError);
        }catch (Exception ex){
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(31)
    public void viewUserProfile_WithoutLogin() {
        String TEST_CASE_NAME = "viewUserProfile_WithoutLogin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            List<String> pathParams = new ArrayList<>();
            pathParams.add("5");

            Util.getStringError(USER.RESOURCE, USER.GET_PROFILE.PATH, pathParams, Error.NOT_AUTHORIZED);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(32)
    public void upgradeRole_Role_InvalidValue() {
        String TEST_CASE_NAME = "upgradeRole_Role_InvalidValue";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData("12");

            String expectErrorMsg = MessageFormat.format(Error.FV.REGEX, payload.getNewRole(),
                    ChangeRoleData.FIELDS.get("newRole"), RegExp.MESSAGES.get(RegExp.LOOKUP));

            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, expectErrorMsg);
            Util.postSecuredFormValidation(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData,
                    payload, pathParams, expected);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(33)
    public void upgradeRole_Role_Required() {
        String TEST_CASE_NAME = "upgradeRole_Role_Required";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData(null);

            String expectErrorMsg = MessageFormat.format(Error.FV.REQUIRED, ChangeRoleData.FIELDS.get("newRole"));

            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, expectErrorMsg);
            Util.postSecuredFormValidation(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData,
                    payload, pathParams, expected);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(34)
    public void upgradeRole_Role_EmptyString() {
        String TEST_CASE_NAME = "upgradeRole_Role_EmptyString";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData("");

            String expectErrorMsg = MessageFormat.format(Error.FV.EMPTY_STR, ChangeRoleData.FIELDS.get("newRole"));

            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, expectErrorMsg);
            Util.postSecuredFormValidation(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData,
                    payload, pathParams, expected);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(35)
    public void upgradeRole_Role_MaxLength() {
        String TEST_CASE_NAME = "upgradeRole_Role_MaxLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData("ASD");

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, payload.getNewRole(),
                    ChangeRoleData.FIELDS.get("newRole"), 2);

            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, expectErrorMsg);
            Util.postSecuredFormValidation(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData,
                    payload, pathParams, expected);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(36)
    public void upgradeRole_Role_MinLength() {
        String TEST_CASE_NAME = "upgradeRole_Role_MinLength";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.STD_USER_FOR_ROLE_UPGRADE);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(STD_USER_ID.toString());

            ChangeRoleData payload = new ChangeRoleData("A");

            String expectErrorMsg = MessageFormat.format(Error.FV.EQ_LENGTH, payload.getNewRole(),
                    ChangeRoleData.FIELDS.get("newRole"), 2);

            FormValidation expected = new FormValidation(CHANGE_ROLE_VALIDATION_ERROR, expectErrorMsg);
            Util.postSecuredFormValidation(USER.RESOURCE, USER.CHANGE_ROLE, Params.adminLoginData,
                    payload, pathParams, expected);
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(37)
    public void viewUserProfile_Tutor_By_Owner() {
        String TEST_CASE_NAME = "viewUserProfile_Tutor_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, TUTOR_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH,
                    Params.ownerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List is null", userProfileData.getRoleList());
            Assert.assertEquals("Role List has more than expected roles", 2, userProfileData.getRoleList().size());

            for (Role role : userProfileData.getRoleList())
                Assert.assertFalse("Role List has Admin Role", role.isAdmin());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(38)
    public void viewUserProfile_Tutor_By_Admin() {
        String TEST_CASE_NAME = "viewUserProfile_Tutor_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, TUTOR_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.adminLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertTrue("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNotNull("Role List is null", userProfileData.getRoleList());
            Assert.assertEquals("Role List has more than expected roles", 2, userProfileData.getRoleList().size());

            for (Role role : userProfileData.getRoleList())
                Assert.assertFalse("Role List has Admin Role", role.isAdmin());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(39)
    public void viewUserProfile_Tutor_By_Student() {
        String TEST_CASE_NAME = "viewUserProfile_Tutor_By_Student";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, TUTOR_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.studentLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(40)
    public void viewUserProfile_Tutor_By_ProfileOwner() {
        String TEST_CASE_NAME = "viewUserProfile_Tutor_By_ProfileOwner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.TUTOR_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, TUTOR_USER_ID, false);

            LoginData profileOwnerLoginData = new LoginData("Test_User_8","123456");

            List<String> pathParams = new ArrayList<>();
            pathParams.add(TUTOR_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, profileOwnerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(41)
    public void viewUserProfile_Admin_By_Owner() {
        String TEST_CASE_NAME = "viewUserProfile_Admin_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, ADMIN_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH,
                    Params.ownerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List is null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(42)
    public void viewUserProfile_Admin_By_Admin() {
        String TEST_CASE_NAME = "viewUserProfile_Admin_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, ADMIN_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.adminLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List is null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(43)
    public void viewUserProfile_Admin_By_Student() {
        String TEST_CASE_NAME = "viewUserProfile_Admin_By_Student";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, ADMIN_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.studentLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(44)
    public void viewUserProfile_Admin_By_ProfileOwner() {
        String TEST_CASE_NAME = "viewUserProfile_Admin_By_ProfileOwner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, ADMIN_USER_ID, false);

            LoginData profileOwnerLoginData = new LoginData("Test_User_6","123456");

            List<String> pathParams = new ArrayList<>();
            pathParams.add(ADMIN_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, profileOwnerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(45)
    public void viewUserProfile_Owner_By_Owner() {
        String TEST_CASE_NAME = "viewUserProfile_Owner_By_Owner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, OWNER_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH,
                    Params.ownerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List is null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(46)
    public void viewUserProfile_Owner_By_Admin() {
        String TEST_CASE_NAME = "viewUserProfile_Owner_By_Admin";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, OWNER_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.adminLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List is null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(47)
    public void viewUserProfile_Owner_By_Student() {
        String TEST_CASE_NAME = "viewUserProfile_Owner_By_Student";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.STUDENT_USER_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, OWNER_USER_ID, false);

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, Params.studentLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertFalse("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't null", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(48)
    public void viewUserProfile_Owner_By_ProfileOwner() {
        String TEST_CASE_NAME = "viewUserProfile_Owner_By_ProfileOwner";
        try {
            AppSession session = appSession.updateSession(CLASS, TEST_CASE_NAME);

            repository.executeScript(Scripts.CLEARING_ALL_TABLES);
            repository.executeScript(Scripts.ROLE_LOOKUP);
            repository.executeScript(Scripts.OWNER_USER_FOR_ROLE_UPGRADE);

            Users profileOwner = dbManager.find(session, Users.class, OWNER_USER_ID, false);
            LoginData profileOwnerLoginData = new LoginData("Test_User_7","123456");

            List<String> pathParams = new ArrayList<>();
            pathParams.add(OWNER_USER_ID.toString());

            Response response = RestUtil.getSecured(USER.RESOURCE, USER.GET_PROFILE.PATH, profileOwnerLoginData, pathParams);
            Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response.getStatus());

            UserProfileData userProfileData = response.readEntity(UserProfileData.class);

            Assert.assertEquals("Full Name isn't correct", profileOwner.getFullName(), userProfileData.getFullName());
            Assert.assertEquals("User Role isn't correct", profileOwner.getRole().getDescription(), userProfileData.getRole());
            Assert.assertEquals("Email isn't correct", profileOwner.getEmail(), userProfileData.getEmail());
            Assert.assertTrue("Can Edit Profile isn't correct", userProfileData.getCanEdit());
            Assert.assertFalse("Can Upgrade Role isn't correct", userProfileData.getCanUpgradeRole());
            Assert.assertNull("Role List isn't correct", userProfileData.getRoleList());
        } catch (Exception ex) {
            Assert.fail(MessageFormat.format(TEST_CASE, TEST_CASE_NAME, ex.getMessage()));
        }
    }

    @Test @InSequence(49)
    public void endClearingAllDBTables() throws Exception{
        repository.executeScript(Scripts.CLEARING_ALL_TABLES);
    }
}
