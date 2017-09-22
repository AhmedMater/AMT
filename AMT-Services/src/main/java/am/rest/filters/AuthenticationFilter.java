package am.rest.filters;

import am.rest.Annotations;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by mohamed.elewa on 5/4/2016.
 */
@Annotations.Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

//    @Inject private UserService userService;
//    @Inject private HttpSession httpSession;

    public void filter(ContainerRequestContext requestContext) throws IOException {
//        AppSession session = null;
//        try {
//            session = new AppSession(httpSession.getId(), Source.REST);
//            AppLogger.startDebugLog(session, AuthenticationFilter.class, "filter");
//
//            String authorizationHeader =
//                    requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//                throw new NotAuthorizedException("Authorization header must be provided");
//            }
//            // Extract the token from the HTTP Authorization header
//            String token = authorizationHeader.substring("Bearer".length()).trim();
//             //Validate the token
//            validateToken(session, token,requestContext);
//
//            AppLogger.endDebugLog(session, AuthenticationFilter.class, "filter");
//        } catch (Exception e) {
//            if(e instanceof BusinessException)
//            {
//                AppLogger.error(session, AuthenticationFilter.class,"filter ", e);
//                throw (BusinessException)e;
//            }
//            AppLogger.error(session, AuthenticationFilter.class, "filter ", e);
//            requestContext.abortWith(
//                    Response.status(Response.Status.UNAUTHORIZED).build());
//        }
    }

//    private final long _expirationMinutes = 60;
//    private void validateToken(AppSession session, String token, ContainerRequestContext requestContext)  {
//        AppLogger.startDebugLog(session, AuthenticationFilter.class," validateToken",token);
//        try {
//            String key = new String(Base64.getDecoder().decode(token),"US-ASCII");
//            String[] parts = key.split(":");
//            if (parts.length == 3)
//            {
//                String hash = parts[0];
//                String username = parts[1];
//                long ticks = Long.parseLong(parts[2]);
//                long diff = new Date().getTime() - ticks;
//                long diffMinutes = diff / (60 * 1000) % 60;
//                if(diffMinutes > _expirationMinutes)
//                {
//                    throw new BusinessException(Response.Status.UNAUTHORIZED,"Token is expired!");
//                }
//                else
//                {
//                    Users userOfToken = userService.getUserByUserName(session, username);
//                    if(userOfToken==null)
//                    {
//                        throw new BusinessException(Response.Status.UNAUTHORIZED,"Invalid User!");
//                    }
//
//                    if(!userOfToken.getActive()){
//                        throw new BusinessException(Response.Status.UNAUTHORIZED,"User Deactivated!");
//                    }
//                    String password = userOfToken.getPassword();
//                    String ComputedHash = SecurityManager.generateToken(username,password,ticks);
//                    if(token.equals(ComputedHash))
//                    {
//                        requestContext.setProperty("Authenticated-User", userOfToken);
//                        session.setUserName(userOfToken.getFirstName() + " " + userOfToken.getLastName());
//                    }else
//                    {
//                        throw new BusinessException(Response.Status.UNAUTHORIZED,"Invalid Token!");
//                    }
//                }
//            }
//            else
//            {
//                throw new BusinessException(Response.Status.UNAUTHORIZED,"Invalid Token!");
//            }
//            AppLogger.endDebugLog(session, AuthenticationFilter.class," validateToken");
//        }catch (Exception ex)
//        {
//            AppLogger.error(session, AuthenticationFilter.class, " validateToken ", ex);
//            if(ex instanceof BusinessException)
//                throw (BusinessException)ex;
//            throw new BusinessException("Exception happened during validate token");
//        }
//    }

}
