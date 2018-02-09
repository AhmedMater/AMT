package am.application;


import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.lookup.Role;
import am.infrastructure.data.hibernate.model.user.Users;
import am.main.api.AppLogger;
import am.main.api.SecurityManager;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.repository.UserRepository;
import am.rest.annotations.Authorized;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

import static am.infrastructure.am.impl.AMTError.*;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Created by Ahmed Mater on 5/9/2017.
 */
public class SecurityService {
    private static final String CLASS = "SecurityService";
    @Inject private UserRepository userRepository;
    @Inject private AppLogger logger;
    @Inject private SecurityManager securityManager;

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
            Authorized authorized = annotatedElement.getAnnotation(Authorized.class);
            if (authorized == null)
                result = new ArrayList<Roles>();
            else {
                Roles[] allowedRoles = authorized.value();
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
            if (allowedRole.description().equals(userRole.getDescription()))
                authorized = true;

        if(!authorized)
            throw new BusinessException(session, UNAUTHORIZED, E_USR_14, authenticatedUser.getFullName());

        logger.endDebug(session);
    }

    public void checkAuthentication(AppSession appSession, ContainerRequestContext requestContext){
        final String FN_NAME = "checkAuthentication";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, requestContext);

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            throw new BusinessException(session, UNAUTHORIZED, E_USR_15, requestContext.getUriInfo().getAbsolutePath().toString());

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        //Validate the token
        Users userOfToken = validateToken(session, token);

        // Set the User in the Request
        requestContext.setProperty("Authenticated-User", userOfToken);

        logger.endDebug(session);
    }

    public Users validateToken(AppSession appSession, String token) {
        String FN_NAME="validateToken";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        try {
            logger.startDebug(session ,token);

            String key = new String(Base64.getDecoder().decode(token),"US-ASCII");
            String[] parts = key.split(":");

            if (parts.length == 3) {
                String hash = parts[0];
                String username = parts[1];
                long ticks = Long.parseLong(parts[2]);

                long diff = new Date().getTime() - ticks;
                long diffMinutes = diff / (60 * 1000) % 60;

                if(diffMinutes > EXPIRATION_MINUTES)
                    throw new BusinessException(session, UNAUTHORIZED, E_USR_16);
                else {
                    Users userOfToken = userRepository.getUserByUserName(session, username);
                    if(userOfToken==null)
                        throw new BusinessException(session, UNAUTHORIZED, E_USR_17, userOfToken.getFullName());

                    String password = userOfToken.getPassword();
                    String ComputedHash = securityManager.generateAccessToken(username, password, ticks);

                    if(!token.equals(ComputedHash))
                        throw new BusinessException(session, UNAUTHORIZED, E_USR_18);

                    logger.endDebug(session, userOfToken);
                    return userOfToken;
                }
            } else
                throw new BusinessException(session, UNAUTHORIZED, E_USR_18);
        }catch (Exception ex){
            if(ex instanceof BusinessException)
                throw (BusinessException)ex;

            throw new BusinessException(session, E_USR_19);
        }
    }

}
