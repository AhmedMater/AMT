package am.rest.filters;


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

    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String FN_NAME = "filter";
//        AppSession session = new AppSession(httpSession.getId(), REST);
//        AppLogger.info(session, CLASS, FN_NAME, IC.INFO_00001, containerRequestContext.getUriInfo().getRequestUri().toString());
    }
}
