package am.rest.resources;

import am.application.CourseService;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.enums.Roles;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.lookup.list.CourseListLookup;
import am.infrastructure.data.view.lookup.list.NewCourseLookup;
import am.infrastructure.data.view.resultset.CourseListRS;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.api.validation.FormValidation;
import am.main.data.dto.ListResultSet;
import am.main.data.dto.SortingInfo;
import am.main.session.AppSession;
import am.rest.annotations.Authorized;
import am.shared.enums.EC;
import am.shared.enums.Forms;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.generic.ConfigParam.AUTH_USER;
import static am.infrastructure.generic.ConfigParam.SOURCE;
import static am.infrastructure.generic.ConfigUtils.businessException;
import static am.shared.enums.Interface.REST;
import static am.shared.enums.Phase.*;

/**
 * Created by ahmed.motair on 9/18/2017.
 */
@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseResource {
    private static final String CLASS = CourseResource.class.getSimpleName();

    @Inject private AppLogger logger;
    @Inject private MessageHandler messageHandler;

    @Inject private HttpSession httpSession;
    @Context private HttpServletRequest httpServletRequest;

    @Inject private CourseService courseService;

    @Path("/new")
    @POST
    @Authorized({Roles.TUTOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewCourse(CourseData courseData, @Context ContainerRequestContext crc) {
        String METHOD = "addNewCourse";
        AppSession session = new AppSession(SOURCE, REST, COURSE_NEW, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
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
        String METHOD = "getCourseList";
        AppSession session = new AppSession(SOURCE, REST, COURSE_LIST, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            // Validating the Form Data
            new FormValidation<CourseListFilter>(session, logger, courseListFilters, EC.AMT_0001, Forms.COURSE_LIST_FILTERS);
            new FormValidation<SortingInfo>(session, logger, courseListFilters.getSorting(), EC.AMT_0001, Forms.COURSE_LIST_FILTERS);

            Users loggedInUser = (Users) crc.getProperty(AUTH_USER);
            ListResultSet<CourseListUI> resultSet = courseService.getCourseList(session, courseListFilters, loggedInUser);
            return Response.ok().entity(new CourseListRS(resultSet)).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/list/lookups")
    @GET
    @Authorized()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseListLookup(@Context ContainerRequestContext crc) {
        String METHOD = "getCourseListLookup";
        AppSession session = new AppSession(SOURCE, REST, COURSE_LIST, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            CourseListLookup result = courseService.getCourseListLookup(session);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0018);
        }
    }

    @Path("/new/lookups")
    @GET
    @Authorized({Roles.TUTOR})
    public Response getNewCourseLookups() {
        String METHOD = "getNewCourseLookups";
        AppSession session = new AppSession(SOURCE, REST, COURSE_NEW, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
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
        String METHOD = "getCourseByID";
        AppSession session = new AppSession(SOURCE, REST, COURSE_DETAIL, httpSession.getId(),
                CLASS, METHOD, httpServletRequest.getRemoteAddr(), messageHandler);
        try {
            CourseData result = courseService.getCourseByID(session, courseID);
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, EC.AMT_0019);
        }
    }


}

