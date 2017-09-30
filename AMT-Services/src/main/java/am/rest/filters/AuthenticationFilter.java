package am.rest.filters;

import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.api.enums.EC;
import am.application.SecurityService;
import am.exception.BusinessException;
import am.rest.Annotations;
import am.session.AppSession;
import am.session.Interface;
import am.session.Phase;
import am.session.Source;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 5/4/2016.
 */
@Annotations.Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String CLASS = "AuthenticationFilter";
    @Inject private SecurityService securityService;
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;
    @Inject private HttpSession httpSession;
    @Inject private AppLogger logger;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHENTICATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler);
        try {
            logger.startDebug(session, requestContext);

            securityService.checkAuthentication(session, requestContext);

            logger.endDebug(session);
        } catch (Exception e) {
            logger.error(session, e);


            if(e instanceof BusinessException)
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build()
                );
            else
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(errorHandler.getMsg(session, EC.AMT_0011)).build()
                );
        }
    }

}
