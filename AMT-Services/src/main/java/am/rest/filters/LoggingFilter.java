package am.rest.filters;


import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.session.AppSession;
import am.shared.enums.IC;
import am.shared.enums.Interface;
import am.shared.enums.Phase;
import am.shared.enums.Source;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 8/3/2016.
 */
@PreMatching
@Priority(900)
public class LoggingFilter implements ContainerRequestFilter {
    private final String CLASS = "LoggingFilter";
    @Inject private HttpSession httpSession;
    @Inject private AppLogger logger;
    @Inject private MessageHandler messageHandler;
    @Context private HttpServletRequest httpServletRequest;

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String FN_NAME = "filter";
        AppSession session = new AppSession(Source.AMT_SERVICES, Interface.REST, Phase.URL_LOGGING,
                httpSession.getId(), CLASS, FN_NAME, httpServletRequest.getRemoteAddr(), messageHandler);
//        String ip = httpServletRequest.getLocalAddr();
//        int ip5 = httpServletRequest.getLocalPort();
//
//        String ip2 = httpServletRequest.getServerName();
//
//        String ip3 = httpServletRequest.getRemoteAddr();
//        String ip4 = httpServletRequest.getRemoteHost();
//        int ip6 = httpServletRequest.getRemotePort();
//
//        System.out.println("Local Address: " + ip);
//        System.out.println("Local Port: " + ip5);
//        System.out.println("Server Name: " + ip2);
//        System.out.println("Remote Address: " + ip3);
//        System.out.println("Remote Port: " + ip6);
//        System.out.println("Remote Host: " + ip4);

        Users authenticatedUser = (Users) requestContext.getProperty("Authenticated-User");
        if(authenticatedUser != null)
            logger.info(session, IC.AMT_0002, authenticatedUser, requestContext.getUriInfo().getRequestUri().toString());
        else
            logger.info(session, IC.AMT_0003, requestContext.getUriInfo().getRequestUri().toString());
    }


}
