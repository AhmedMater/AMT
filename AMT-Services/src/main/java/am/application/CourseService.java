package am.application;

import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.course.CoursePRData;
import am.infrastructure.data.dto.course.CourseRefData;
import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.lookup.ContentStatus;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.lookup.list.CourseListLookup;
import am.infrastructure.data.view.lookup.list.NewCourseLookup;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AppLogger;
import am.main.api.JMSManager;
import am.main.api.SecurityManager;
import am.main.api.db.DBManager;
import am.main.api.validation.FormValidation;
import am.main.data.dto.ListResultSet;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.repository.CourseRepository;
import am.shared.dto.AMDestination;
import am.shared.dto.AMNotification;
import am.shared.enums.EC;
import am.shared.enums.Forms;
import am.shared.enums.notification.AMEventNotifications;
import am.shared.enums.notification.AMEvents;
import am.shared.enums.notification.Attributes;

import javax.inject.Inject;
import java.util.*;

import static am.infrastructure.data.enums.ContentStatus.FUTURE;
import static am.shared.enums.JMSQueues.NOTIFICATION_INPUT;
import static am.shared.enums.notification.AMEventNotifications.*;
import static am.shared.enums.notification.Attributes.*;

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

    public void validatedNewCourseData(AppSession appSession, CourseData courseData){
        String FN_NAME = "validatedNewCourseData";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseData);

        // Validating the Form Data
        new FormValidation<CourseData>(session, logger, courseData, EC.AMT_0001, Forms.NEW_COURSE);

        List<CoursePRData> coursePRDataList = courseData.getPreRequisites();
        for (CoursePRData coursePRData :coursePRDataList)
            new FormValidation<CoursePRData>(session, logger, coursePRData, EC.AMT_0001, Forms.NEW_COURSE);

        List<CourseRefData> courseRefDataList = courseData.getReferences();
        for (CourseRefData courseRefData :courseRefDataList)
            new FormValidation<CourseRefData>(session, logger, courseRefData, EC.AMT_0001, Forms.NEW_COURSE);

//        logger.info(session, IC.AMT_0001, Forms.NEW_COURSE);
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
            throw new BusinessException(session, EC.AMT_0041);

        String courseID = courseRepository.generateCourseID(session, course);
        course.setCourseID(courseID);

        course = dbManager.persist(session, course, true);


        logger.endDebug(session, courseID);
        return courseID;
    }

    public void generateNewCourseNotification(AppSession appSession, Course course) throws Exception{

        Map<Attributes, String> parameters = new HashMap<>();
        parameters.put(COURSE_ID, course.getCourseID());
        parameters.put(COURSE_NAME, course.getCourseName());
        parameters.put(COURSE_LEVEL, course.getCourseLevel().getDescription());
        parameters.put(COURSE_TYPE, course.getCourseType().getDescription());
        parameters.put(COURSE_ESTIMATED_DURATION, course.getEstimatedDuration().toString() + " Hr");

        Map<AMEventNotifications, List<AMDestination>> emails = new HashMap<>();

        List<AMDestination> interestedStudents = new ArrayList<>();
        interestedStudents.add(new AMDestination("ahmedmotair@gmail.com", "01273024235", "1234"));
        interestedStudents.add(new AMDestination("amrmotair@gmail.com", "01273024235", "1234"));

        List<AMDestination> interestedTechRev = new ArrayList<>();
        interestedTechRev.add(new AMDestination("manal.mahmoud.gouda@gmail.com", "01001997640", "2234"));

        List<AMDestination> interestedTranslators = new ArrayList<>();
        interestedTranslators.add(new AMDestination("night.wolf2015@yahoo.com", "01174254232", "934"));

        emails.put(IS, interestedStudents);
        emails.put(ITR, interestedTechRev);
        emails.put(ITT, interestedTranslators);

        AMNotification notification = new AMNotification();
        notification.setEvent(AMEvents.NEW_COURSE);
        notification.setCategoryRelatedID(course.getCourseID());
        notification.setParameters(parameters);
        notification.setDestinations(emails);

        jmsManager.sendMessage(NOTIFICATION_INPUT, notification);
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

    public ListResultSet<CourseListUI> getCourseList(AppSession appSession, CourseListFilter courseListFilters, Users loggedInUser) throws Exception{
        String FN_NAME = "getCourseList";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseListFilters, loggedInUser);

        ListResultSet<CourseListUI> resultSet = new ListResultSet<CourseListUI>();
        resultSet = courseRepository.getAllCourses(session, courseListFilters);

        logger.endDebug(session, resultSet);
        return resultSet;
    }
}
