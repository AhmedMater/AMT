package am.rest.filters;


import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.api.enums.IC;
import am.infrastructure.data.hibernate.model.user.Users;
import am.session.AppSession;
import am.session.Interface;
import am.session.Phase;
import am.session.Source;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 8/3/2016.
 */
@PreMatching
public class LoggingFilter implements ContainerRequestFilter {
    private final String CLASS = "LoggingFilter";
    @Inject private HttpSession httpSession;
    @Inject private AppLogger logger;
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.URL_LOGGING, httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler);

        Users authenticatedUser = (Users) requestContext.getProperty("Authenticated-User");
        if(authenticatedUser != null)
            logger.info(session, IC.AMT_0002, authenticatedUser, requestContext.getUriInfo().getRequestUri().toString());
        else
            logger.info(session, IC.AMT_0003, requestContext.getUriInfo().getRequestUri().toString());
    }
}
