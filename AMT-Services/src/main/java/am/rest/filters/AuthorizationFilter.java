package am.rest.filters;


import am.rest.Annotations;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 5/4/2016.
 */
@Annotations.Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {
    @Context private ResourceInfo resourceInfo;

//    @Inject private HttpSession httpSession;

    public void filter(ContainerRequestContext requestContext) throws IOException {
//        AppSession session = null;
//        try {
//            session = new AppSession(httpSession.getId(), Source.REST);
//            AppLogger.startDebugLog(session, AuthorizationFilter.class, "filter");
//            Class<?> resourceClass = resourceInfo.getResourceClass();
//            List<Roles> classRoles = extractRoles(session, resourceClass);
//
//            // Get the resource method which matches with the requested URL
//            // Extract the roles declared by it
//            Method resourceMethod = resourceInfo.getResourceMethod();
//            List<Roles> methodRoles = extractRoles(session, resourceMethod);
//
//            // Check if the user is allowed to execute the method
//            // The method annotations override the class annotations
//            if (methodRoles.isEmpty()) {
//                if(classRoles.isEmpty()){
//                    AppLogger.endDebugLog(session, AuthorizationFilter.class, "filter");
//                    return;
//                }else {
//                    checkPermissions(session, classRoles, requestContext);
//                }
//            } else {
//                checkPermissions(session, methodRoles,requestContext);
//                AppLogger.endDebugLog(session, AuthorizationFilter.class, "filter");
//            }
//
//        } catch (Exception e) {
//            //AppLogger.debug(getUserInfo(), AuthorizationFilter.class.getName() + " filter "+ ExceptionUtils.exceptionStackTraceAsString(e));
//            AppLogger.error(session, AuthorizationFilter.class, " filter ", e);
//            requestContext.abortWith(
//                    Response.status(Response.Status.FORBIDDEN).build());
//        }
    }


//    // Extract the roles from the annotated element
//    private List<Roles> extractRoles(AppSession session, AnnotatedElement annotatedElement) {
//        AppLogger.startDebugLog(session, AuthorizationFilter.class, "extractRoles",annotatedElement);
//        if (annotatedElement == null) {
//            return new ArrayList<>();
//        } else {
//            Annotations.Secured secured = annotatedElement.getAnnotation(Annotations.Secured.class);
//            if (secured == null) {
//                AppLogger.endDebugLog(session, AuthorizationFilter.class, "extractRoles");
//                return new ArrayList<>();
//            } else {
//                Roles[] allowedRoles = secured.value();
//                AppLogger.endDebugLog(session, AuthorizationFilter.class, "extractRoles");
//                return Arrays.asList(allowedRoles);
//            }
//        }
//    }
//
//    private void checkPermissions(AppSession session, List<Roles> allowedRoles, ContainerRequestContext requestContext) throws Exception {
//        // Check if the user contains one of the allowed roles
//        // Throw an Exception if the user has not permission to execute the method
//        AppLogger.startDebugLog(session, AuthorizationFilter.class, "checkPermissions",allowedRoles);
//        Users authenticatedUser = (Users)requestContext.getProperty("Authenticated-User");
//        Set<Role> userRoles = authenticatedUser.getRoles();
//
//        boolean authorized = false;
//        for(int i = 0; i< allowedRoles.size(); i++)
//        {
//            for (Role userRole : userRoles) {
//                if (allowedRoles.get(i).description().equals(userRole.getDescription()))
//                    authorized = true;
//            }
//        }
//        if(!authorized) {
//            AppLogger.endDebugLog(session, AuthorizationFilter.class, "checkPermissions");
//            throw new Exception("Not Authorized");
//        }
//        AppLogger.endDebugLog(session, AuthorizationFilter.class, "checkPermissions");
//            //AuthenticationEndpoint.txt += allowedRoles.get(i);
//    }
}
