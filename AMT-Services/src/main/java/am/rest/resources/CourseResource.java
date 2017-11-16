package am.rest.resources;

import am.main.api.components.AppLogger;
import am.main.api.components.ErrorHandler;
import am.main.api.components.InfoHandler;
import am.application.CourseService;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.view.NewCourseLookup;
import am.rest.annotations.Secured;
import am.main.session.AppSession;
import am.main.session.Interface;
import am.shared.session.Phase;
import am.main.session.Source;

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
    @Secured({Roles.TUTOR})
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
    @Secured({Roles.TUTOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewCourseLookups(@Context HttpServletRequest requestContext)  throws Exception{
        String FN_NAME = "getNewCourseLookups";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        NewCourseLookup result = courseService.getNewCourseLookup(session);
        return Response.ok().entity(result).build();
    }



    @Path("/{courseID}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseByID(
            @PathParam("courseID") String courseID,
            @Context HttpServletRequest requestContext)  throws Exception{
        String FN_NAME = "getCourseByID";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.REGISTRATION, CLASS, FN_NAME, errorHandler, infoHandler);
        CourseData result = courseService.getCourseByID(session, courseID);
        return Response.ok().entity(result).build();
    }


}

