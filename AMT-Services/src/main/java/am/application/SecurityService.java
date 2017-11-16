package am.application;


import am.main.api.components.AMSecurityManager;
import am.main.api.components.AppLogger;
import am.shared.enums.EC;
import am.main.exception.BusinessException;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.Users;
import am.repository.UserRepository;
import am.rest.annotations.Secured;
import am.main.session.AppSession;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Ahmed Mater on 5/9/2017.
 */
public class SecurityService {
    private static final String CLASS = "SecurityService";
    @Inject private UserRepository userRepository;
    @Inject private AppLogger logger;
    @Inject private AMSecurityManager securityManager;

    private final long EXPIRATION_MINUTES = 60;

    public void checkAuthorization(AppSession appSession, ResourceInfo resourceInfo, ContainerRequestContext requestContext) throws Exception{
        final String FN_NAME = "checkAuthorization";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, requestContext);

        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Roles> classRoles = extractRoles(session, resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Roles> methodRoles = extractRoles(session, resourceMethod);

        // Check if the user is allowed to execute the method
        // The method annotations override the class annotations
        if (methodRoles.isEmpty()) {
            if(!classRoles.isEmpty())
                checkPermissions(session, classRoles, requestContext);
        } else
            checkPermissions(session, methodRoles,requestContext);

        logger.endDebug(session);
    }

    // Extract the roles from the annotated element
    private List<Roles> extractRoles(AppSession appSession, AnnotatedElement annotatedElement) throws Exception {
        final String FN_NAME = "extractRoles";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, annotatedElement);

        List<Roles> result;

        if (annotatedElement == null)
            result = new ArrayList<Roles>();
        else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null)
                result = new ArrayList<Roles>();
            else {
                Roles[] allowedRoles = secured.value();
                result = Arrays.asList(allowedRoles);
            }
        }

        logger.endDebug(session, result);
        return result;
    }

    /**
     * Check if the user contains one of the allowed roles
     * Throw an Exception if the user has not permission to execute the method
     * @param allowedRoles
     * @param requestContext
     * @throws Exception
     */
    private void checkPermissions(AppSession appSession, List<Roles> allowedRoles, ContainerRequestContext requestContext) throws Exception {
        String FN_NAME = "checkPermissions";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, allowedRoles, requestContext);

        Users authenticatedUser = (Users) requestContext.getProperty("Authenticated-User");
        Role userRole = authenticatedUser.getRole();

        boolean authorized = false;
        for (Roles allowedRole : allowedRoles)
            if (allowedRole.name().equals(userRole.getDescription()))
                authorized = true;

        if(!authorized)
            throw new NotAuthorizedException(session, EC.AMT_0005, authenticatedUser.getFullName());

        logger.endDebug(session);
    }

    public void checkAuthentication(AppSession appSession, ContainerRequestContext requestContext){
        final String FN_NAME = "checkAuthentication";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, requestContext);

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new NotAuthorizedException(session, EC.AMT_0006, requestContext.getUriInfo().getAbsolutePath().toString());

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        //Validate the token
        Users userOfToken = validateToken(session, token, requestContext);

        // Set the User in the Request
        requestContext.setProperty("Authenticated-User", userOfToken);

        logger.endDebug(session);
    }

    private Users validateToken(AppSession appSession, String token, ContainerRequestContext requestContext) {
        String FN_NAME="validateToken";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        try {
            logger.startDebug(session ,token, requestContext);

            String key = new String(Base64.getDecoder().decode(token),"US-ASCII");
            String[] parts = key.split(":");

            if (parts.length == 3) {
                String hash = parts[0];
                String username = parts[1];
                long ticks = Long.parseLong(parts[2]);

                long diff = new Date().getTime() - ticks;
                long diffMinutes = diff / (60 * 1000) % 60;

                if(diffMinutes > EXPIRATION_MINUTES)
                    throw new BusinessException(session, Response.Status.UNAUTHORIZED, EC.AMT_0007);
                else {
                    Users userOfToken = userRepository.getUserByUserName(session, username);
                    if(userOfToken==null)
                        throw new BusinessException(session, Response.Status.UNAUTHORIZED, EC.AMT_0008, userOfToken.getFullName());

                    String password = userOfToken.getPassword();
                    String ComputedHash = securityManager.generateToken(session, username, password, ticks);

                    if(!token.equals(ComputedHash))
                        throw new BusinessException(session, Response.Status.UNAUTHORIZED, EC.AMT_0010);

                    logger.endDebug(session, userOfToken);
                    return userOfToken;
                }
            } else
                throw new BusinessException(session, Response.Status.UNAUTHORIZED, EC.AMT_0010);
        }catch (Exception ex){
            if(ex instanceof BusinessException)
                throw (BusinessException)ex;

            throw new BusinessException(session, ex, EC.AMT_0004);
        }
    }

}
