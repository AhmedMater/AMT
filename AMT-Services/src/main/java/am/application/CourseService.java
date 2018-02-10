package am.application;

import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.course.CoursePRData;
import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.lookup.ContentStatus;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.lookup.list.CourseListLookup;
import am.infrastructure.data.view.lookup.list.NewCourseLookup;
import am.main.api.AppLogger;
import am.main.api.JMSManager;
import am.main.api.SecurityManager;
import am.main.api.db.DBManager;
import am.main.api.validation.FormValidation;
import am.main.data.dto.LoginData;
import am.main.data.dto.notification.AMDestination;
import am.main.data.dto.notification.AMNotificationData;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.repository.CourseRepository;

import javax.inject.Inject;
import java.util.*;

import static am.infrastructure.am.AMTForms.NEW_COURSE;
import static am.infrastructure.am.impl.ASE.E_COR_1;
import static am.infrastructure.am.impl.ASQ.NOTIFICATION;
import static am.infrastructure.data.enums.ContentStatus.FUTURE;
import static am.main.data.enums.impl.AME.E_VAL_0;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseService {
    private static final String CLASS = "CourseService";

    @Inject private AppLogger logger;
    @Inject private CourseRepository courseRepository;
    @Inject private SecurityManager securityManager;
    @Inject private JMSManager jmsManager;
    @Inject private DBManager dbManager;

    public void validatedNewCourseData(AppSession appSession, CourseData courseData) throws Exception {
        String FN_NAME = "validatedNewCourseData";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseData);

        // Validating the Form Data
        new FormValidation<CourseData>(session, logger, courseData, E_VAL_0, NEW_COURSE);

        List<CoursePRData> coursePRDataList = courseData.getPreRequisites();
        for (CoursePRData coursePRData :coursePRDataList)
            new FormValidation<CoursePRData>(session, logger, coursePRData, E_VAL_0, NEW_COURSE);

        List<CourseRefData> courseRefDataList = courseData.getReferences();
        for (CourseRefData courseRefData :courseRefDataList)
            new FormValidation<CourseRefData>(session, logger, courseRefData, E_VAL_0, NEW_COURSE);

        logger.endDebug(session);
    }

    public String addNewCourse(AppSession appSession, CourseData courseData, Users tutorUser) throws Exception{
        String FN_NAME = "addNewCourse";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseData);

        Course course = new Course(session, dbManager, courseData);
        course.setCreationDate(new Date());
        course.setCreatedBy(tutorUser);
        course.setActualDuration(0);
        course.setCourseStatus(new ContentStatus(FUTURE.status()));

        if(!courseRepository.canTutorAddNewCourse(session, tutorUser))
            throw new BusinessException(session, E_COR_1);

        String courseID = courseRepository.generateCourseID(session, course);
        course.setCourseID(courseID);

        course = dbManager.persist(session, course, true);

        logger.endDebug(session, courseID);
        return courseID;
    }

    public void generateNewCourseNotification(AppSession appSession, Course course) throws Exception{
        String FN_NAME = "generateNewCourseNotification";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, course);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("COURSE_ID", course.getCourseID());
        parameters.put("COURSE_NAME", course.getCourseName());
        parameters.put("COURSE_LEVEL", course.getCourseLevel().getDescription());
        parameters.put("COURSE_TYPE", course.getCourseType().getDescription());
        parameters.put("COURSE_ESTIMATED_DURATION", course.getEstimatedDuration().toString() + " Hr");

        List<AMDestination> destinations = new ArrayList<>();

        destinations.add(new AMDestination(Arrays.asList("IS", "ITR"), "ahmedmotair@gmail.com", "01273024235", "Ahmed Mater", "231"));
        destinations.add(new AMDestination(Arrays.asList("ITT", "ITR"), "amrmotair@gmail.com", "01273024235", "Amr Mater", "1234"));
        destinations.add(new AMDestination(Arrays.asList("IS"), "manal.mahmoud.gouda@gmail.com", "01001997640", "Manal Mahmoud", "41"));
        destinations.add(new AMDestination(Arrays.asList("ITR"), "night.wolf2015@gmail.com", "01174254232", "Manal Mahmoud", "934"));

        AMNotificationData notification = new AMNotificationData();
        notification.setLoginData(new LoginData());
        notification.setCategory("COR");
        notification.setCategoryRelatedID(course.getCourseID());
        notification.setParameters(parameters);
        notification.setDestinations(destinations);

        jmsManager.sendObjMessage(session, NOTIFICATION, notification);
    }

    public NewCourseLookup getNewCourseLookup(AppSession appSession) throws Exception{
        String FN_NAME = "getNewCourseLookup";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session);

        NewCourseLookup result = new NewCourseLookup();

        List<MaterialType> materialTypeList = dbManager.findAll(session, MaterialType.class, true);
        result.setMaterialTypeList(materialTypeList);

        List<CourseLevel> courseLevelList = dbManager.findAll(session, CourseLevel.class, true);
        result.setCourseLevelList(courseLevelList);

        List<CourseType> courseTypeList = dbManager.findAll(session, CourseType.class, true);
        result.setCourseTypeList(courseTypeList);

        logger.endDebug(session, result);
        return result;
    }

    public CourseListLookup getCourseListLookup(AppSession appSession) throws Exception{
        String FN_NAME = "getCourseListLookup";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session);

        CourseListLookup result = new CourseListLookup();

        List<CourseLevel> courseLevelList = dbManager.findAll(session, CourseLevel.class, true);
        result.setCourseLevels(courseLevelList);

        List<CourseType> courseTypeList = dbManager.findAll(session, CourseType.class, true);
        result.setCourseTypes(courseTypeList);

        logger.endDebug(session, result);
        return result;
    }

    public CourseData getCourseByID(AppSession appSession, String courseID) throws Exception{
        String FN_NAME = "getCourseByID";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseID);

        Course course = dbManager.find(session, Course.class, courseID, false);
        CourseData courseData = new CourseData(course);

        logger.endDebug(session, courseData);
        return courseData;
    }

//    public ListResultSet<CourseListUI> getCourseList(AppSession appSession, CourseListFilter courseListFilters, Users loggedInUser) throws Exception{
//        String FN_NAME = "getCourseList";
//        AppSession session = appSession.updateSession(CLASS, FN_NAME);
//        logger.startDebug(session, courseListFilters, loggedInUser);
//
//
//        logger.endDebug(session, resultSet);
//        return resultSet;
//    }
}
