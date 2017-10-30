package am.rest.resources;

import am.api.components.AppConfigManager;
import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.api.components.db.DBManager;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by ahmed.motair on 9/18/2017.
 */
@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    @Inject private ErrorHandler errorHandler;
    @Inject private InfoHandler infoHandler;
    @Inject private AppConfigManager appConfigManager;
    @Inject private HttpSession httpSession;
    @Inject private AppLogger logger;
    @Inject private DBManager dbManager;

    private String CLASS = "CourseResource";

    @Path("/am")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String getCourseByID(@Context HttpServletRequest requestContext)  {
        return requestContext.getRemoteAddr();
//        String FN_NAME = "getCourseByID";
//        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHENTICATION,
//                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler);
//
//        logger.startDebug(session, "Ahmed");
//        String output = errorHandler.getMsg(session, EC.AMT_0001, "Ahmed");
//        output += "\n" + infoHandler.getMsg(session, IC.AMT_0001);
//        try {
//            output += "\n" + appConfigManager.getConfigValue(session, App_CC.LOGGER_NAME, String.class);
//        }catch (Exception ex){
//            logger.error(session, ex, EC.AMT_0000);
//        }
//        logger.info(session, IC.AMT_0002, "Ahmed Mater", "http://localhost:8080/AMT-Services/api/course/am");
//
//        logger.endDebug(session, "Ali");
//        return "success";
    }

    @Path("/ahmed")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getAhmedByID() {
//        String FN_NAME = "getAhmedByID";
//        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.AUTHENTICATION,
//                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler);
//
//        Role role = new Role("D", "Doctor");
//
//        try {
//            dbManager.persist(session, role, true);
//        } catch (Exception e) {
//            logger.error(session, e);
//        }
//        try {
//            role = dbManager.find(session, Role.class, "A", true);
//        } catch (DBException e) {
//            logger.error(session, e);
//        }
//
//        logger.endDebug(session, "Ali");
//        return role.toString();
        return "success";
    }
}

