package am.rest.resources;

import am.api.components.AppLogger;
import am.api.components.ErrorHandler;
import am.api.components.InfoHandler;
import am.application.CourseService;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.hibernate.view.NewCourseLookup;
import am.session.AppSession;
import am.session.Interface;
import am.session.Phase;
import am.session.Source;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ahmed.motair on 9/18/2017.
 */
@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    private static final String CLASS = "CourseResource";

    @Inject private ErrorHandler errorHandler;
    @Inject private CourseService courseService;
    @Inject private InfoHandler infoHandler;
    @Inject private AppLogger logger;

    @Path("/new")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewCourse(
            CourseData courseData,
            @Context HttpServletRequest requestContext) throws Exception {
        String FN_NAME = "addNewCourse";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        try {
            courseService.addNewCourse(session, courseData);
            return Response.ok().build();
        }catch (Exception ex){
            logger.error(session, ex);
            throw ex;
        }
    }

    @Path("/new/lookups")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewCourseLookups(@Context HttpServletRequest requestContext)  throws Exception{
        String FN_NAME = "getNewCourseLookups";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        NewCourseLookup result = courseService.getNewCourseLookup(session);
        return Response.ok().entity(result).build();
    }


}

