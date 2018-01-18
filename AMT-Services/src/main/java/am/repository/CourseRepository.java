package am.repository;

import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.hibernate.model.SystemParameter;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.hibernate.model.user.Users;
import am.infrastructure.data.view.ui.CourseListUI;
import am.infrastructure.generic.ConfigParam;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.api.db.HQLCondition;
import am.main.api.db.QueryBuilder;
import am.main.data.dto.ListResultSet;
import am.main.session.AppSession;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static am.infrastructure.data.enums.ContentStatus.FUTURE;
import static am.infrastructure.data.enums.ContentStatus.IN_PROGRESS;
import static am.infrastructure.generic.ConfigParam.MAX_PAGE_SIZE;
import static am.main.data.enums.Operators.EQ;
import static am.main.data.enums.Operators.LIKE;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
public class CourseRepository {
    private static final String CLASS = "UserRepository";
    @Inject private DBManager dbManager;
    @Inject private AppLogger logger;

    @Transactional
    public String generateCourseID(AppSession appSession, Course course) throws Exception{
        String METHOD = "generateCourseID";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        logger.startDebug(session, course);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(SystemParameter.PARAM_NAME, ConfigParam.COURSE_NUM_PARAM);

        SystemParameter parameter = dbManager.getSingleResult(session, true,
                SystemParameter.class, parameters);

        String courseNumStr = parameter.getParamValue();
        Integer courseNum = Integer.parseInt(courseNumStr) + 1;

        if(courseNumStr.length() != 7){
            courseNumStr = StringUtils.repeat("0", 7 - courseNumStr.length()) + courseNum.toString();
        }else
            courseNumStr = courseNum.toString();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String courseID = "Cor" + courseNumStr + course.getCourseType().getType() +
                course.getCourseLevel().getLevel() + df.format(course.getCreationDate());

        parameter.setParamValue(Integer.toString(courseNum));
        dbManager.merge(session, parameter, true);

        logger.endDebug(session, courseID);
        return courseID;
    }
    public Boolean userHasCourses(AppSession appSession, Integer userID) throws Exception{
        String METHOD = "userHasCourses";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        logger.startDebug(session, userID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Course.COURSE_CREATOR_USER_ID, userID);

        List<Course> courses = dbManager.getList(session, true,
                Course.class, parameters);

        Boolean result = (courses.size() > 0);

        logger.endDebug(session, result);
        return result;
    }

    public ListResultSet<CourseListUI> getAllCourses(AppSession appSession, CourseListFilter filters){
        String METHOD = "getAllCourses";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        logger.startDebug(session, filters);

        String selectData = "SELECT new am.infrastructure.data.view.ui.CourseListUI(courseID, courseName, " +
            "courseLevel.level, courseStatus.status, estimatedDuration, actualDuration, " +
            "concat(createdBy.firstName, concat(' ', createdBy.lastName)), startDate, progress) ";
        String from = "FROM " + Course.CLASS;

        QueryBuilder<CourseListUI> queryBuilder = new QueryBuilder<CourseListUI>(CourseListUI.class, logger, session);
        queryBuilder.setDataSelect(selectData);
        queryBuilder.setFrom(from);

        queryBuilder.addCondition(new HQLCondition<String>(Course.COURSE_NAME, LIKE, filters.getCourseName()));
        queryBuilder.addCondition(new HQLCondition<String>(Course.COURSE_LEVEL, EQ, filters.getCourseLevel()));
        queryBuilder.addCondition(new HQLCondition<String>(Course.COURSE_TYPE, EQ, filters.getCourseType()));

        queryBuilder.setSorting(filters.getSorting());
        queryBuilder.setPagingInfo(filters.getPageNum(), MAX_PAGE_SIZE);

        EntityManager em = dbManager.getUnCachedEM();
        queryBuilder.executeQuery(em);

        ListResultSet<CourseListUI> resultSet = queryBuilder.getResultSet();

        logger.endDebug(session, resultSet);
        return resultSet;
    }

    public boolean canTutorAddNewCourse(AppSession appSession, Users tutorUser) {
        String METHOD = "canTutorAddNewCourse";
        AppSession session = appSession.updateSession(CLASS, METHOD);
        logger.startDebug(session, tutorUser);

        boolean result = true;

        String selectData = "";
        String from = "FROM " + Course.CLASS;

        QueryBuilder<CourseListUI> queryBuilder = new QueryBuilder<CourseListUI>(CourseListUI.class, logger, session);
        queryBuilder.setDataSelect(selectData);
        queryBuilder.setFrom(from);

        queryBuilder.addCondition(new HQLCondition<Integer>(Course.COURSE_CREATOR_USER_ID, EQ, tutorUser.getUserID()));
        queryBuilder.addCondition(new HQLCondition<String>(Course.COURSE_STATUS, FUTURE.status(), IN_PROGRESS.status()));

        queryBuilder.setSorting(new CourseListFilter().getSorting());
        queryBuilder.setPagingInfo(0, MAX_PAGE_SIZE);

        EntityManager em = dbManager.getUnCachedEM();
        queryBuilder.executeQuery(em);

        ListResultSet<CourseListUI> courseListRS = queryBuilder.getResultSet();
        if(courseListRS.getData().size() >= 3)
            result = false;

        logger.endDebug(session, result);
        return result;
    }
}
