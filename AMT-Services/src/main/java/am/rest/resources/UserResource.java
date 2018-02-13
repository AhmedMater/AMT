package am.rest.resources;

import am.application.UserService;
import am.infrastructure.data.dto.filters.UserListFilter;
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
import am.repository.UserRepository;
import am.rest.annotations.Authorized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Date;

import static am.infrastructure.am.AMTForms.LOGIN;
import static am.infrastructure.am.AMTForms.REGISTER;
import static am.infrastructure.am.AMTForms.USER_LIST_FILTERS;
import static am.infrastructure.am.impl.ASE.*;
import static am.infrastructure.am.impl.ASI.*;
import static am.infrastructure.am.impl.ASP.*;
import static am.infrastructure.generic.ConfigParam.AUTH_USER;
import static am.infrastructure.generic.ConfigParam.SOURCE;
import static am.infrastructure.generic.ConfigUtils.businessException;
import static am.main.data.enums.Interface.REST;
import static am.main.data.enums.impl.AME.E_VAL_0;

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
    @Inject private UserRepository userRepository;

    private Logger INITIAL_LOGGER = LogManager.getLogger("Initial");

    @Path("/register")
    @POST
    public Response register(UserRegisterData userRegisterData) {
        String METHOD = "register";
        AppSession session = new AppSession(SOURCE, REST, USER_REGISTRATION, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{
            logger.info(session, I_USR_1);
            new FormValidation<UserRegisterData>(session, logger, userRegisterData, E_VAL_0, REGISTER);

            userService.register(session, userRegisterData, Roles.STUDENT);
            logger.info(session, I_USR_2, userRegisterData.fullName());
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_USR_3, userRegisterData.fullName());
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
//            new FormValidation<UserRegisterData>(session, adminUserData, E_VAL_0, Forms.REGISTER);
//            loggerList.info(session, IC.AMT_0001, Forms.LOGIN);
//
//            userService.register(session, adminUserData, Roles.ADMIN);
//            return Response.ok().build();
//        }catch (Exception ex){
//            throw businessException(loggerList, session, ex, EC.AMT_0050, adminUserData.fullName());
//        }
//    }

    @Path("/login")
    @POST
    public Response login(LoginData loginData) {
        String METHOD = "login";
        AppSession session = new AppSession(SOURCE, REST, USER_LOGIN, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try{
            long startTime = new Date().getTime();
            logger.info(session, I_USR_5, loginData.getUsername());
            new FormValidation<LoginData>(session, logger, loginData, E_VAL_0, LOGIN);

            String loginUserIP = httpServletRequest.getRemoteAddr();
            AuthenticatedUser user = userService.login(session, loginData, loginUserIP);

            logger.info(session, I_USR_6, user.getFullName());

            long endTime = new Date().getTime();
            long diff = endTime - startTime;

            logger.info(session, I_USR_2, "Message : took " + diff + " mSec = " + (diff/1000) + " Sec");
            return Response.ok().entity(user).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_USR_8, loginData.getUsername());
        }
    }

//    @Path("/profile/changeRole/{ownerID}")
//    @POST
//    @Authorized({Roles.ADMIN, Roles.OWNER})
//    public Response changeUserRole(@PathParam("ownerID") String ownerID, ChangeRoleData changeRoleData) {
//        String METHOD = "changeUserRole";
//        AppSession session = new AppSession(SOURCE, REST, USER_DETAIL, httpSession.getId(),
//                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
//        try{
//            // Validating the Form Data
//            new FormValidation<ChangeRoleData>(session, logger, changeRoleData, E_VAL_0, Forms.CHANGE_ROLE);
//
//            userService.changeRole(session, changeRoleData.getNewRole(), ownerID);
//            return Response.ok().build();
//        }catch (Exception ex){
//            throw businessException(logger, session, ex, EC.AMT_0025, ownerID);
//        }
//    }

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
            logger.info(session, I_USR_7, ownerID);

            Users viewer = (Users) crc.getProperty(AUTH_USER);
            UserProfileData userProfileData = userService.getProfileData(session, ownerID, viewer);

            logger.info(session, I_USR_8, userProfileData.getFullName());
            return Response.ok().entity(userProfileData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_USR_11, ownerID);
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
            logger.info(session, I_USR_9);

            new FormValidation<UserListFilter>(session, logger, userListFilters, E_VAL_0, USER_LIST_FILTERS);
            new FormValidation<SortingInfo>(session, logger, userListFilters.getSorting(), E_VAL_0, USER_LIST_FILTERS);

            Users loggedInUser = (Users) crc.getProperty(AUTH_USER);

            ListResultSet<UserListUI> resultSet = new ListResultSet<UserListUI>();
            resultSet = userRepository.getAllUser(session, userListFilters);

            logger.info(session, I_USR_10);
            return Response.ok().entity(new UserListRS(resultSet)).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_USR_12);
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
            throw businessException(logger, session, ex, E_USR_13);
        }
    }
}
