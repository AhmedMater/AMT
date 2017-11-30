package am.rest.filters;


import am.main.api.AppLogger;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.application.SecurityService;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.main.data.enums.Interface;
import am.shared.session.Phase;
import am.main.data.enums.Source;

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
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;
    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHORIZATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
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
                    Response.status(Response.Status.UNAUTHORIZED).entity(errorHandler.getMsg(session, EC.AMT_0012)).build()
                );
        }
    }
}
