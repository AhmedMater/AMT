package am.rest.resources;

import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.api.enums.EC;
import am.application.UserService;
import am.infrastructure.generic.ConfigUtils;
import am.session.AppSession;
import am.session.Interface;
import am.session.Phase;
import am.session.Source;

import javax.inject.Inject;
import javax.ws.rs.*;
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

    @Path("/register")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("email") String email) throws Exception {
        String FN_NAME = "register";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try{
            logger.startDebug(session, firstName, lastName, username, (password == null) ? "Null" : "Password", email);
            userService.register(session, firstName, lastName, username, password, email);
            logger.endDebug(session);
            return Response.ok().build();
        }catch (Exception ex){
            Exception exc = ConfigUtils.businessException(logger, session, ex, EC.AMT_0013, firstName + " " + lastName);
            throw exc;
        }
    }
}
