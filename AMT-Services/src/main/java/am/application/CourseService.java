package am.application;

import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.lookup.ContentStatus;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.lookup.list.CourseListFilters;
import am.infrastructure.data.view.lookup.list.NewCourseLookup;
import am.infrastructure.data.view.ui.CourseListUI;
import am.main.api.AMSecurityManager;
import am.main.api.AppConfigManager;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.api.validation.FormValidation;
import am.main.data.dto.ListResultSet;
import am.main.session.AppSession;
import am.repository.CourseRepository;
import am.shared.enums.EC;
import am.shared.enums.Forms;
import am.shared.enums.IC;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static am.infrastructure.data.enums.ContentStatus.FUTURE;

/**
 * Created by ahmed.motair on 11/6/2017.
 */
public class CourseService {
    private static final String CLASS = "CourseService";

    @Inject private AppLogger logger;
    @Inject private CourseRepository courseRepository;
    @Inject private AMSecurityManager securityManager;
    @Inject private AppConfigManager appConfigManager;
    @Inject private DBManager dbManager;

    public void validatedNewCourseData(AppSession appSession, CourseData courseData){
        String FN_NAME = "validatedNewCourseData";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseData);

        // Validating the Form Data
        new FormValidation<CourseData>(session, courseData, EC.AMT_0001, Forms.NEW_COURSE);

//        List<CoursePRData> coursePRDataList = courseData.getPreRequisites();
//        for (CoursePRData coursePRData :coursePRDataList)
//            new FormValidation<CoursePRData>(session, coursePRData, EC.AMT_0001, Forms.NEW_COURSE);
//
//        List<CourseRefData> courseRefDataList = courseData.getReferences();
//        for (CourseRefData courseRefData :courseRefDataList)
//            new FormValidation<CourseRefData>(session, courseRefData, EC.AMT_0001, Forms.NEW_COURSE);

        logger.info(session, IC.AMT_0001, Forms.NEW_COURSE);
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

//        Integer numOfDays = (int) Math.ceil((courseData.getEstimatedDuration() * 60) / courseData.getEstimatedMinPerDay());
//        LocalDateTime dueDateTime = LocalDateTime.ofInstant(courseData.getStartDate().toInstant(), ZoneId.systemDefault())
//                .plusDays(numOfDays);
//        course.setDueDate(Date.from(dueDateTime.atZone(ZoneId.systemDefault()).toInstant()));

        String courseID = courseRepository.generateCourseID(session, course);
        course.setCourseID(courseID);

        course = dbManager.persist(session, course, true);
        logger.endDebug(session, courseID);
        return courseID;
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

    public CourseListFilters getCourseListFilters(AppSession appSession) throws Exception{
        String FN_NAME = "getCourseListFilters";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session);

        CourseListFilters result = new CourseListFilters();

        List<CourseLevel> courseLevelList = dbManager.findAll(session, CourseLevel.class, true);
        result.setCourseLevelList(courseLevelList);

        List<CourseType> courseTypeList = dbManager.findAll(session, CourseType.class, true);
        result.setCourseTypeList(courseTypeList);

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

//    @Transactional
    public ListResultSet<CourseListUI> getCourseList(AppSession appSession, CourseListFilter courseListFilters, Users loggedInUser) throws Exception{
        String FN_NAME = "addNewCourse";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseListFilters, loggedInUser);

        ListResultSet<CourseListUI> resultSet = new ListResultSet<CourseListUI>();
        resultSet = courseRepository.getAllCourses(session, courseListFilters);

        logger.endDebug(session, resultSet);
        return resultSet;
    }
}
