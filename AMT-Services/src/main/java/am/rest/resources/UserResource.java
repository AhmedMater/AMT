package am.rest.resources;

import am.main.api.components.AppLogger;
import am.main.api.components.ErrorHandler;
import am.main.api.components.InfoHandler;
import am.shared.enums.EC;
import am.application.UserService;
import am.main.exception.BusinessException;
import am.infrastructure.data.dto.LoginData;
import am.infrastructure.data.dto.UserRegisterData;
import am.infrastructure.data.view.AuthenticatedUser;
import am.infrastructure.generic.ConfigUtils;
import am.main.session.AppSession;
import am.main.session.Interface;
import am.shared.session.Phase;
import am.main.session.Source;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Path("/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserRegisterData userRegisterData) throws Exception {
        String FN_NAME = "register";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            userService.register(session, userRegisterData);
            return Response.ok().build();
        }catch (Exception ex){
            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, userRegisterData.fullName());
            throw exc;
        }
    }


    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginData loginData, @Context HttpServletRequest requestContext) throws BusinessException {
        String FN_NAME = "login";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            String loginUserIP = requestContext.getRemoteAddr();
            AuthenticatedUser user = userService.login(session, loginData, loginUserIP);
            return Response.ok().entity(user).build();
        }catch (Exception ex){
            BusinessException exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0017, loginData.getUsername());
            throw exc;
        }
    }
}
