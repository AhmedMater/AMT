package am.rest.resources;

import am.application.UserService;
import am.infrastructure.data.dto.user.ChangeRoleData;
import am.infrastructure.data.dto.user.LoginData;
import am.infrastructure.data.dto.user.UserRegisterData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.view.AuthenticatedUser;
import am.infrastructure.data.view.UserProfileData;
import am.main.api.AppLogger;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.data.enums.Interface;
import am.main.data.enums.Source;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.rest.annotations.Secured;
import am.shared.enums.EC;
import am.shared.enums.IC;
import am.shared.session.Phase;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserRegisterData userRegisterData) throws BusinessException {
        String FN_NAME = "register";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<UserRegisterData>(session, EC.AMT_0021, userRegisterData);
            logger.info(session, IC.AMT_0006);

            userService.register(session, userRegisterData);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
        }
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginData loginData) throws BusinessException {
        String FN_NAME = "login";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.LOGIN,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<LoginData>(session, EC.AMT_0022, loginData);
            logger.info(session, IC.AMT_0001);

            String loginUserIP = httpServletRequest.getRemoteAddr();
            AuthenticatedUser user = userService.login(session, loginData, loginUserIP);
            return Response.ok().entity(user).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0017, loginData.getUsername());
        }
    }

    @Path("/profile/changeRole")
    @POST
    @Secured({Roles.ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserRole(ChangeRoleData changeRoleData) throws BusinessException {
        String FN_NAME = "changeUserRole";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.USER_UPDATE,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            // Validating the Form Data
            new FormValidation<ChangeRoleData>(session, EC.AMT_0028, changeRoleData);
//            logger.info(session, IC.AMT_0001);

            userService.changeRole(session, changeRoleData);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0025, changeRoleData.getOwnerUserID());
        }
    }

    @Path("/profile/{ownerID}/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfileData(
            @PathParam("ownerID") String ownerID,
            @QueryParam("viewerID") String viewerID) throws BusinessException {
        String FN_NAME = "getUserProfileData";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.USER_VIEW,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            UserProfileData userProfileData = userService.getProfileData(session, ownerID, viewerID);
            return Response.ok().entity(userProfileData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0024, ownerID);
        }
    }
}
