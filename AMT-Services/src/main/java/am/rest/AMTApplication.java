package am.rest;

import am.rest.filters.AuthorizationFilter;
import am.rest.filters.CORSResponseFilter;
import am.rest.filters.LoggingFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ApplicationPath;

//import am.rest.filters.AuthenticationFilter;

/**
 * Created by ahmed.motair on 9/9/2017.
 */
@ApplicationPath("/api")
public class AMTApplication extends ResourceConfig {

    public AMTApplication() {
        try {

            //register filters
            register(CORSResponseFilter.class);
//            register(AuthenticationFilter.class);
            register(AuthorizationFilter.class);
            register(LoggingFilter.class);

            // Specify REST Resources packages
            packages(getClass().getPackage().getName() + ".resources");
            register(MultiPartFeature.class);
            register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bindFactory(HttpSessionFactory.class).to(HttpSession.class)
                        .proxy(true).proxyForSameScope(false).in(RequestScoped.class);
                }
            });



//            } else {
//                AppLogger.error(null, NPCApplication.class, "NPCApplication", "Not Valid License!");
//                throw new NPCBusinessException("Not valid NPC License!");
//            }

        } catch (Exception ex) {
//            AppLogger.error(null, AMTApplication.class, "NPCApplication", ex);
//            if (ex instanceof BusinessException)
//                throw (BusinessException) ex;
//            throw new BusinessException(NPCErrorHandler.getConfig(null, NPCError.SOP0008E, ex.getMessage()), ex);
        }

    }
}
