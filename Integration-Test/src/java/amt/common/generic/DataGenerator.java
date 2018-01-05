package amt.common.generic;

import am.application.SecurityService;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.main.api.AMSecurityManager;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.api.db.DBManager;
import am.main.session.AppSession;
import am.shared.enums.Phase;
import amt.common.constants.Rest;
import amt.common.enums.Scripts;
import org.junit.Assert;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static am.main.data.enums.Interface.ARQUILLIAN;
import static am.main.data.enums.Source.INTEGRATION_TEST;

/**
 * Created by ahmed.motair on 11/23/2017.
 */
public class DataGenerator {
    @Inject private Repository repository;
    @Inject private DBManager dbManager;
    @Inject private AMSecurityManager securityManager;
    @Inject private SecurityService securityService;

    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    private static final String CLASS = "DataGenerator";

    private AppSession appSession = new AppSession(INTEGRATION_TEST, ARQUILLIAN, Phase.INTEGRATION_TEST, errorHandler, infoHandler);

    /**
     * <p>Calling the User Registering REST to insert new User in the Database and confirm its insertion</p>
     * Requires Loading Scripts
     *<blockquote>
     *  1. Clearing All Tables Script<p>
     *  2. Role Lookup Script
     *</blockquote>
     * @param data UserRegisterData Object that will be inserted
     *
     */
    public Users registerUser(UserRegisterData data) throws Exception{
        String FN_NAME = "registerUser";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);

        Response response = RestUtil.post(Rest.USER.RESOURCE, Rest.USER.REGISTER, data);
        Assert.assertEquals("Register New User failed", Response.Status.OK.getStatusCode(), response.getStatus());

        Users actual = repository.getUserByUsername(data.getUsername());

        Assert.assertEquals("First name failed", data.getFirstName(), actual.getFirstName());
        Assert.assertEquals("Last name failed", data.getLastName(), actual.getLastName());
        Assert.assertEquals("Username failed", data.getUsername(), actual.getUsername());
        Assert.assertEquals("Password failed", securityManager.dm5Hash(session, data.getPassword()), actual.getPassword());
        Assert.assertEquals("Email failed", data.getEmail(), actual.getEmail());
        Assert.assertEquals("Role failed", Roles.STUDENT.role(), actual.getRole().getRole());

        if(!Util.isEqualDates(new Date(), actual.getCreationDate()))
            Assert.fail("Creation Date failed");
        return actual;
    }

    /**
     * <p>Calling the User Login</p>
     * @param data LoginData Object with Username and Password
     *  @return String token of the login
     */
    public String login(LoginData data) throws Exception{
        String FN_NAME = "data";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);

        Response response = RestUtil.post(Rest.USER.RESOURCE, Rest.USER.LOGIN, data);
        Assert.assertEquals("Register New User failed", Response.Status.OK.getStatusCode(), response.getStatus());

        AuthenticatedUser actual = response.readEntity(AuthenticatedUser.class);
        Assert.assertNotNull("Authenticated User isn't returned", actual);

        Users user = repository.getUserByUsername(data.getUsername());

        Assert.assertEquals("Full Name isn't correct", user.getFullName(), actual.getFullName());
        Assert.assertEquals("User ID isn't correct", user.getUserID(), actual.getUserID());
        Assert.assertEquals("User Role isn't correct", user.getRole().getRole(), actual.getRole());
        Assert.assertNotNull("User Token isn't generated", actual.getToken());
        Assert.assertNotNull("User Token isn't valid", securityService.validateToken(session, actual.getToken()));

        return actual.getToken();
    }

    public LoginData getAdminLoginData() throws Exception{
        repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
        Users adminUser = dbManager.find(appSession, Users.class, 2, false);
        return new LoginData(adminUser.getUsername(), "123456");
    }

    public LoginData getOwnerLoginData() throws Exception{
        repository.executeScript(Scripts.ADMIN_USER_LOOKUP);
        Users adminUser = dbManager.find(appSession, Users.class, 1, false);
        return new LoginData(adminUser.getUsername(), "123456");
    }

    public void upgradeUserRole_ToTutor(Integer studentUserID) throws Exception{
        LoginData adminLoginData = getAdminLoginData().clone();

        List<String> pathParams = new ArrayList<>();
        pathParams.add(studentUserID.toString());

        Response response1 = RestUtil.postSecured(Rest.USER.RESOURCE, Rest.USER.CHANGE_ROLE, Roles.TUTOR.role(), pathParams, adminLoginData);
        Assert.assertEquals("Viewing User Profile REST CAll Response isn't correct", Response.Status.OK.getStatusCode(), response1.getStatus());

        Users tutorUser = dbManager.find(appSession, Users.class, studentUserID, false);
        Assert.assertEquals("Role hasn't been changed", Roles.TUTOR.role(), tutorUser.getRole().getRole());
    }
}
