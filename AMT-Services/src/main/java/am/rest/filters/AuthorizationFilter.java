package am.rest.filters;


import am.application.SecurityService;
import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.data.enums.Interface;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;

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

import static am.infrastructure.am.impl.AMTError.E_USR_20;
import static am.infrastructure.am.impl.AMTPhase.AUTHORIZATION;
import static am.infrastructure.generic.ConfigParam.SOURCE;

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
        AppSession session = new AppSession(SOURCE, Interface.REST, AUTHORIZATION,
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
                    Response.status(Response.Status.UNAUTHORIZED).entity(E_USR_20.getFullMsg(session.getMessageHandler())).build()
                );
        }
    }
}
