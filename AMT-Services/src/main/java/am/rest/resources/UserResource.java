package am.rest.resources;

import am.application.UserService;
import am.infrastructure.data.dto.filters.UserListFilter;
import am.infrastructure.data.dto.user.ChangeRoleData;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.AuthenticatedUser;
import am.infrastructure.data.view.UserProfileData;
import am.infrastructure.data.view.lookup.list.UserListLookup;
import am.infrastructure.data.view.resultset.UserListRS;
import am.infrastructure.data.view.ui.UserListUI;
import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.api.validation.FormValidation;
import am.main.data.dto.ListResultSet;
import am.main.data.dto.SortingInfo;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.shared.enums.Forms;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.generic.ConfigParam.AUTH_USER;
import static am.infrastructure.generic.ConfigParam.SOURCE;
import static am.infrastructure.generic.ConfigUtils.businessException;
import static am.shared.enums.Interface.REST;
import static am.shared.enums.Phase.*;

/**
 * Created by ahmed.motair on 9/23/2017.
 */
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final String CLASS = UserResource.class.getSimpleName();

    @Inject private MessageHandler messageHandler;
    @Inject private AppLogger logger;

    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    @Inject private UserService userService;

    @Path("/register")
    @POST
    public Response register(UserRegisterData userRegisterData) {
        String METHOD = "register";
        AppSession session = new AppSession(SOURCE, REST, USER_REGISTRATION, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{
            // Validating the Form Data
            new FormValidation<UserRegisterData>(session, logger, userRegisterData, EC.AMT_0001, Forms.REGISTER);

            userService.register(session, userRegisterData, Roles.STUDENT);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0050, userRegisterData.fullName());
        }
    }

//    @Path("/admin/register")
//    @POST
//    @Authorized(Roles.OWNER)
//    public Response createAdmin(UserRegisterData adminUserData) {
//        String METHOD = "createAdmin";
//        AppSession session = new AppSession(Source.APP_SERVICES, REST, Phase.REGISTRATION,
//                httpSession.getId(), CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
//        try{
//            // Validating the Form Data
//            new FormValidation<UserRegisterData>(session, adminUserData, EC.AMT_0001, Forms.REGISTER);
//            logger.info(session, IC.AMT_0001, Forms.LOGIN);
//
//            userService.register(session, adminUserData, Roles.ADMIN);
//            return Response.ok().build();
//        }catch (Exception ex){
//            throw businessException(logger, session, ex, EC.AMT_0050, adminUserData.fullName());
//        }
//    }

    @Path("/login")
    @POST
    public Response login(LoginData loginData) {
        String METHOD = "login";
        AppSession session = new AppSession(SOURCE, REST, USER_LOGIN, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{

            long startTime = System.currentTimeMillis();
            // Validating the Form Data
            new FormValidation<LoginData>(session, logger, loginData, EC.AMT_0001, Forms.LOGIN);

            String loginUserIP = httpServletRequest.getRemoteAddr();
            AuthenticatedUser user = userService.login(session, loginData, loginUserIP);

            long endTime = System.currentTimeMillis();
            System.out.println("Login took " + (endTime - startTime) + " mSec\n");
            return Response.ok().entity(user).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0017, loginData.getUsername());
        }
    }

    @Path("/profile/changeRole/{ownerID}")
    @POST
    @Authorized({Roles.ADMIN, Roles.OWNER})
    public Response changeUserRole(@PathParam("ownerID") String ownerID, ChangeRoleData changeRoleData) {
        String METHOD = "changeUserRole";
        AppSession session = new AppSession(SOURCE, REST, USER_DETAIL, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{
            // Validating the Form Data
            new FormValidation<ChangeRoleData>(session, logger, changeRoleData, EC.AMT_0001, Forms.CHANGE_ROLE);

            userService.changeRole(session, changeRoleData.getNewRole(), ownerID);
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
        String METHOD = "getUserProfileData";
        AppSession session = new AppSession(SOURCE, REST, USER_DETAIL, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{
            Users viewer = (Users) crc.getProperty(AUTH_USER);
            UserProfileData userProfileData = userService.getProfileData(session, ownerID, viewer);
            return Response.ok().entity(userProfileData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0024, ownerID);
        }
    }

    @Path("/list")
    @POST
    @Authorized({Roles.ADMIN, Roles.OWNER})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserList(UserListFilter userListFilters, @Context ContainerRequestContext crc) {
        String METHOD = "getUserList";
        AppSession session = new AppSession(SOURCE, REST, USER_LIST, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            // Validating the Form Data
            new FormValidation<UserListFilter>(session, logger, userListFilters, EC.AMT_0001, Forms.USER_LIST_FILTERS);
            new FormValidation<SortingInfo>(session, logger, userListFilters.getSorting(), EC.AMT_0001, Forms.USER_LIST_FILTERS);

            Users loggedInUser = (Users) crc.getProperty(AUTH_USER);
            ListResultSet<UserListUI> resultSet = userService.getUserList(session, userListFilters, loggedInUser);
            return Response.ok().entity(new UserListRS(resultSet)).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/list/lookups")
    @GET
    @Authorized({Roles.ADMIN, Roles.OWNER})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserListLookup(@Context ContainerRequestContext crc) {
        String METHOD = "getUserListLookup";
        AppSession session = new AppSession(SOURCE, REST, USER_LIST, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            UserListLookup result = userService.getUserListLookup(session);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }
}
