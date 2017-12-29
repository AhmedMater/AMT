package am.rest.resources;

import am.application.CourseService;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.lookup.list.CourseListFilters;
import am.infrastructure.data.view.lookup.list.NewCourseLookup;
import am.infrastructure.data.view.resultset.CourseListRS;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AppLogger;
import am.main.api.ErrorHandler;
import am.main.api.InfoHandler;
import am.main.data.dto.ListResultSet;
import am.main.data.enums.Interface;
import am.main.data.enums.Source;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.shared.session.Phase;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.generic.ConfigParam.AUTH_USER;
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
    @Authorized({Roles.TUTOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewCourse(CourseData courseData, @Context ContainerRequestContext crc) {
        String FN_NAME = "addNewCourse";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_CREATE,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try {
            courseService.validatedNewCourseData(session, courseData);

            Users tutorUser = (Users) crc.getProperty(AUTH_USER);
            String courseID = courseService.addNewCourse(session, courseData, tutorUser);

            courseData.setCourseID(courseID);
            return Response.ok().entity(courseData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/list")
    @POST
    @Authorized()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseList(CourseListFilter courseListFilters, @Context ContainerRequestContext crc) {
        String FN_NAME = "getCourseList";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_VIEW,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try {
            Users loggedInUser = (Users) crc.getProperty(AUTH_USER);
            ListResultSet<CourseListUI> resultSet = courseService.getCourseList(session, courseListFilters, loggedInUser);
//            GenericEntity<ListResultSet<CourseListUI>> genericEntity =
//                    new GenericEntity<ListResultSet<CourseListUI>>(resultSet) {
//            };//needs empty body to preserve generic type
            return Response.ok().entity(new CourseListRS(resultSet)).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/list/filters")
    @GET
    @Authorized()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseListFilters(@Context ContainerRequestContext crc) {
        String FN_NAME = "getCourseListFilters";
        AppSession session = new AppSession(Source.APP_SERVICES, Interface.REST, Phase.COURSE_VIEW,
                httpSession.getId(), CLASS, FN_NAME, errorHandler, infoHandler, httpServletRequest.getRemoteAddr());
        try {
            CourseListFilters result = courseService.getCourseListFilters(session);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/new/lookups")
    @GET
    @Authorized({Roles.TUTOR})
    public Response getNewCourseLookups() {
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
    @Authorized
    public Response getCourseByID(@PathParam("courseID") String courseID)  {
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

