package am.rest.resources;

import am.application.CourseService;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.view.NewCourseLookup;
import am.main.api.AppLogger;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.common.validation.FormValidation;
import am.main.data.enums.Interface;
import am.main.data.enums.Source;
import am.main.session.AppSession;
import am.rest.annotations.Secured;
import am.shared.enums.EC;
import am.shared.enums.Forms;
import am.shared.enums.IC;
import am.shared.session.Phase;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.generic.ConfigUtils.businessException;

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
    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    @Path("/new")
    @POST
    @Secured({Roles.TUTOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewCourse(CourseData courseData) throws Exception {
        String FN_NAME = "addNewCourse";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_CREATE,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try {
            // Validating the Form Data
            new FormValidation<CourseData>(session, courseData, EC.AMT_0001, Forms.NEW_COURSE);
            logger.info(session, IC.AMT_0001, Forms.NEW_COURSE);

            courseService.addNewCourse(session, courseData);
            return Response.ok().build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/new/lookups")
    @GET
    @Secured({Roles.TUTOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewCourseLookups()  throws Exception{
        String FN_NAME = "getNewCourseLookups";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_CREATE,
            httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try{
            NewCourseLookup result = courseService.getNewCourseLookup(session);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0019);
        }
    }

    @Path("/{courseID}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseByID(@PathParam("courseID") String courseID)  throws Exception{
        String FN_NAME = "getCourseByID";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_VIEW,
            httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try {
            CourseData result = courseService.getCourseByID(session, courseID);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0019);
        }
    }


}

