package am.rest.resources;

import am.api.error.EC;
import am.api.error.ErrorHandler;
import am.api.info.IC;
import am.api.info.InfoHandler;
import am.api.logger.AppLogger;
import am.api.sysConfig.AppConfigManager;
import am.api.sysConfig.App_CC;
import am.session.AppSession;
import am.session.Phase;
import am.session.Source;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by ahmed.motair on 9/18/2017.
 */
@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    @Inject private ErrorHandler errorHandler;
    @Inject private AppConfigManager appConfigManager;
    @Inject private InfoHandler infoHandler;


    @Path("/am")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getCourseByID()  {

        AppSession session = new AppSession(Phase.INITIAL_APP, Source.AM_API, errorHandler, infoHandler);

        String output = errorHandler.getMsg(EC.DB_0001, "Ahmed");
        output += "\n" + infoHandler.getMsg(IC.ENM_001);
        try {
            output += "\n" + appConfigManager.getConfigValue(App_CC.LOGGER_NAME, String.class);
        }catch (Exception ex){
            AppLogger.error(session, getClass().getSimpleName(), "getCourseByID", ex, EC.DB_0001);
        }

        return output;
    }
}

