package am.rest.filters;


import am.application.SecurityService;
import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.shared.enums.Interface;
import am.shared.enums.Phase;
import am.shared.enums.Source;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 5/4/2016.
 */

@Authorized
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    private static final String CLASS = "AuthorizationFilter";
    @Context private ResourceInfo resourceInfo;
    @Inject private SecurityService securityService;
    @Inject private AppLogger logger;
    @Inject private MessageHandler messageHandler;
    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.AMT_SERVICES, Interface.REST, Phase.AUTHORIZATION,
                httpSession.getId(), CLASS, FN_NAME, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            logger.startDebug(session);

            securityService.checkAuthentication(session, requestContext);
            securityService.checkAuthorization(session, resourceInfo, requestContext);

            logger.endDebug(session);
        } catch (Exception e) {
            logger.error(session, e);

            if(e instanceof BusinessException)
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build()
                );
            else
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity(session.getErrorMsg(EC.AMT_0036)).build()
                );
        }
    }
}
