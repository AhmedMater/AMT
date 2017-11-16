package am.rest.filters;


import am.main.api.components.AppLogger;
import am.main.api.components.ErrorHandler;
import am.main.api.components.InfoHandler;
import am.shared.enums.EC;
import am.application.SecurityService;
import am.main.exception.BusinessException;
import am.rest.annotations.Secured;
import am.main.session.AppSession;
import am.main.session.Interface;
import am.shared.session.Phase;
import am.main.session.Source;

import javax.annotation.Priority;
import javax.inject.Inject;
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


@Secured
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

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHORIZATION,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler);
        try {
            logger.startDebug(session);

            securityService.checkAuthorization(session, resourceInfo, requestContext);

            logger.endDebug(session);
        } catch (Exception e) {
            logger.error(session, e);

            if(e instanceof BusinessException)
                requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build()
                );
            else
                requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).entity(errorHandler.getMsg(session, EC.AMT_0012)).build()
                );
        }
    }
}
