//package am.rest.filters;
//
//import am.application.SecurityService;
//import am.main.api.AppLogger;
//import am.main.api.ErrorHandler;
//import am.main.api.InfoHandler;
//import am.main.data.am.shared.enums.Interface;
//import am.main.data.am.shared.enums.Source;
//import am.main.exception.BusinessException;
//import am.main.am.shared.session.AppSession;
//import am.shared.am.shared.enums.EC;
//import am.shared.am.shared.session.Phase;
//
//import javax.annotation.Priority;
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.ResourceInfo;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//import java.io.IOException;
//
///**
// * Created by mohamed.elewa on 5/4/2016.
// */
////@Authorized
//@Provider
//@Priority(Priorities.AUTHENTICATION)
//public class AuthenticationFilter implements ContainerRequestFilter {
//    private static final String CLASS = "AuthenticationFilter";
//    @Inject private SecurityService securityService;
//    @Inject private ErrorHandler errorHandler;
//    @Inject private InfoHandler infoHandler;
//    @Inject private HttpSession httpSession;
//    @Inject private AppLogger logger;
//    @Context private ResourceInfo resourceInfo;
//    @Context private HttpServletRequest httpServletRequest;
//
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//        String FN_NAME = "filter";
//        AppSession am.shared.session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHENTICATION,
//                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
//        try {
//            logger.startDebug(am.shared.session, requestContext);
//
//            securityService.checkAuthentication(am.shared.session, requestContext);
//
//            logger.endDebug(am.shared.session);
//        } catch (Exception e) {
//            logger.error(am.shared.session, e);
//
//            if (e instanceof BusinessException)
//                requestContext.abortWith(
//                        Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build()
//                );
//            else
//                requestContext.abortWith(
//                        Response.status(Response.Status.UNAUTHORIZED).entity(errorHandler.getMsg(am.shared.session, EC.AMT_0011)).build()
//                );
//        }
//
//    }
//}
