package am.application;

import am.main.api.AMSecurityManager;
import am.main.api.AppConfigManager;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.infrastructure.data.dto.course.CourseData;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.lookup.CourseLevel;
import am.infrastructure.data.hibernate.model.lookup.CourseType;
import am.infrastructure.data.hibernate.model.lookup.MaterialType;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.NewCourseLookup;
import am.repository.CourseRepository;
import am.main.session.AppSession;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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

    @Transactional
    public String addNewCourse(AppSession appSession, CourseData courseData) throws Exception{
        String FN_NAME = "addNewCourse";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseData);

        Course course = new Course(courseData);
        course.setCreationDate(new Date());
        course.setCreatedBy(new Users(1));
        course.setCompleted(false);

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

    public CourseData getCourseByID(AppSession appSession, String courseID) throws Exception{
        String FN_NAME = "getCourseByID";
        AppSession session = appSession.updateSession(CLASS, FN_NAME);
        logger.startDebug(session, courseID);

        Course course = dbManager.find(session, Course.class, courseID, false);
        CourseData courseData = new CourseData(course);

        logger.endDebug(session, courseData);
        return courseData;
    }
}
