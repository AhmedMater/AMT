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
import am.repository.CourseRepository;
import am.rest.annotations.Authorized;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static am.infrastructure.am.AMTForms.COURSE_LIST_FILTERS;
import static am.infrastructure.am.impl.AMTError.*;
import static am.infrastructure.am.impl.AMTInfo.*;
import static am.infrastructure.am.impl.AMTPhase.*;
import static am.infrastructure.generic.ConfigParam.AUTH_USER;
import static am.infrastructure.generic.ConfigParam.SOURCE;
import static am.infrastructure.generic.ConfigUtils.businessException;
import static am.main.data.enums.Interface.REST;
import static am.main.data.enums.impl.IEC.E_VAL_0;
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
    @Inject private CourseRepository courseRepository;

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
            logger.info(session, I_COR_1);
            courseService.validatedNewCourseData(session, courseData);

            Users tutorUser = (Users) crc.getProperty(AUTH_USER);
            String courseID = courseService.addNewCourse(session, courseData, tutorUser);

            courseData.setCourseID(courseID);
            logger.info(session, I_COR_2, courseData.getCourseName());
            return Response.ok().entity(courseData).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_COR_2);
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
            logger.info(session, I_COR_5);
            // Validating the Form Data
            new FormValidation<CourseListFilter>(session, logger, courseListFilters, E_VAL_0, COURSE_LIST_FILTERS);
            new FormValidation<SortingInfo>(session, logger, courseListFilters.getSorting(), E_VAL_0, COURSE_LIST_FILTERS);

            Users loggedInUser = (Users) crc.getProperty(AUTH_USER);

            ListResultSet<CourseListUI> resultSet = new ListResultSet<CourseListUI>();
            resultSet = courseRepository.getAllCourses(session, courseListFilters);

            logger.info(session, I_COR_6);
            return Response.ok().entity(new CourseListRS(resultSet)).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_COR_3);
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
            throw businessException(logger, session, ex, E_COR_6);
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
            throw businessException(logger, session, ex, E_COR_5);
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
            logger.info(session, I_COR_3);
            CourseData result = courseService.getCourseByID(session, courseID);
            logger.info(session, I_COR_4, result.getCourseName());
            return Response.ok().entity(result).build();
        }catch (Exception ex){
            throw businessException(logger, session, ex, E_COR_4);
        }
    }


}

