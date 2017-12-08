package am.rest.resources;

import am.application.UserService;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.infrastructure.data.view.UserProfileData;
import am.main.api.AppLogger;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.common.validation.RegExp;
import am.main.common.validation.groups.BlankValidation;
import am.main.common.validation.groups.InvalidValidation;
import am.main.common.validation.groups.LengthValidation;
import am.main.common.validation.groups.RequiredValidation;
import am.main.data.enums.Interface;
import am.main.data.enums.Source;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.shared.enums.Forms;
import am.shared.enums.IC;
import am.shared.session.Phase;
import org.hibernate.validator.constraints.Length;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.generic.ConfigParam.AUTH_USER;
import static am.infrastructure.generic.ConfigUtils.businessException;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final String CLASS = "UserResource";
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;
    @Inject private AppLogger logger;
    @Inject private UserService userService;
    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    @Path("/register")
    @POST
    public Response register(UserRegisterData userRegisterData) {
        String FN_NAME = "register";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<UserRegisterData>(session, userRegisterData, EC.AMT_0001, Forms.REGISTER);
            logger.info(session, IC.AMT_0001, Forms.LOGIN);

            userService.register(session, userRegisterData, Roles.STUDENT);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0050, userRegisterData.fullName());
        }
    }

    @Path("/admin/register")
    @POST
    @Authorized(Roles.OWNER)
    public Response createAdmin(UserRegisterData adminUserData) {
        String FN_NAME = "createAdmin";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<UserRegisterData>(session, adminUserData, EC.AMT_0001, Forms.REGISTER);
            logger.info(session, IC.AMT_0001, Forms.LOGIN);

            userService.register(session, adminUserData, Roles.ADMIN);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0050, adminUserData.fullName());
        }
    }

    @Path("/login")
    @POST
    public Response login(LoginData loginData) {
        String FN_NAME = "login";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.LOGIN,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<LoginData>(session, loginData, EC.AMT_0001, Forms.LOGIN);
            logger.info(session, IC.AMT_0001, Forms.LOGIN);

            String loginUserIP = httpServletRequest.getRemoteAddr();
            AuthenticatedUser user = userService.login(session, loginData, loginUserIP);
            return Response.ok().entity(user).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0017, loginData.getUsername());
        }
    }

    @Path("/profile/changeRole/{ownerID}")
    @POST
    @Authorized({Roles.ADMIN, Roles.OWNER})
    public Response changeUserRole(@PathParam("ownerID") String ownerID,

            @NotNull(message = am.main.common.validation.groups.FormValidation.REQUIRED, groups = RequiredValidation.class)
            @Length(min = 2, max = 2, message = am.main.common.validation.groups.FormValidation.MIN_MAX_LENGTH, groups = LengthValidation.class)
            @Pattern(regexp = RegExp.LOOKUP, message = am.main.common.validation.groups.FormValidation.REGEX, groups = InvalidValidation.class)
            @NotEmpty(message = am.main.common.validation.groups.FormValidation.EMPTY_STR, groups = BlankValidation.class)
                    String newRole) {
        String FN_NAME = "changeUserRole";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.USER_UPDATE,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<String>(session, newRole, EC.AMT_0001, Forms.CHANGE_ROLE);
            logger.info(session, IC.AMT_0001, Forms.CHANGE_ROLE);

            userService.changeRole(session, newRole, ownerID);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0025, ownerID);
        }
    }

    @Path("/profile/{ownerID}/")
    @GET
    @Authorized()
    public Response getUserProfileData(
            @PathParam("ownerID") String ownerID,
            @Context ContainerRequestContext crc) {
        String FN_NAME = "getUserProfileData";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.USER_VIEW,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            Users viewer = (Users) crc.getProperty(AUTH_USER);
            UserProfileData userProfileData = userService.getProfileData(session, ownerID, viewer);
            return Response.ok().entity(userProfileData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0024, ownerID);
        }
    }
}
