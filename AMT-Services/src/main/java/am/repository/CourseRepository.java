package am.repository;

import am.infrastructure.data.dto.filters.CourseListFilter;
import am.infrastructure.data.hibernate.model.SystemParameter;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.data.view.resultset.CourseListRS;
import am.infrastructure.data.view.ui.CourseListUI;
import am.infrastructure.generic.ConfigParam;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.data.dto.filter.SortingInfo;
import am.main.data.vto.PaginationInfo;
import am.main.session.AppSession;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static am.infrastructure.generic.ConfigParam.MAX_PAGE_SIZE;

/**
 * Created by ahmed.motair on 11/7/2017.
 */
public class CourseRepository {
    private static final String CLASS = "UserRepository";
    @Inject private DBManager dbManager;
    @Inject private AppLogger logger;

    @Transactional
    public String generateCourseID(AppSession session, Course course) throws Exception{
        String FN_NAME = "generateCourseID";
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
    public Boolean userHasCourses(AppSession session, Integer userID) throws Exception{
        String FN_NAME = "userHasCourses";
        logger.startDebug(session, userID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Course.COURSE_CREATOR_USER_ID, userID);

        List<Course> courses = dbManager.getList(session, true,
                Course.class, parameters);

        Boolean result = (courses.size() > 0);

        logger.endDebug(session, result);
        return result;
    }

//    @Transactional
    public CourseListRS getAllCourses(AppSession session, CourseListFilter filters){
        String FN_NAME = "getAllCourses";
        logger.startDebug(session, filters);

        EntityManager em = dbManager.getUnCachedEM();
        SortingInfo sorting = filters.getSortingInfo();

        String hql = "SELECT new am.infrastructure.data.view.ui.CourseListUI(courseID, courseName, courseLevel.level, " +
                "courseStatus.status, estimatedDuration, actualDuration, " +
                "concat(createdBy.firstName, concat(' ', createdBy.lastName)), " +
                "startDate, progress) FROM Course " +
                "ORDER BY " + sorting.getBy() + " " + sorting.getDirection();

        List<CourseListUI> resultData = em.createQuery(hql, CourseListUI.class)
            .setMaxResults(MAX_PAGE_SIZE)
            .setFirstResult(filters.getPageNum() * MAX_PAGE_SIZE)
            .getResultList();

        Integer resultCount = em.createQuery(
                "SELECT COUNT(*) FROM Course", Long.class)
                .getResultList().get(0).intValue();

        CourseListRS resultSet = new CourseListRS();
        resultSet.setData(resultData);
        resultSet.setPaginationInfo(new PaginationInfo(resultCount, MAX_PAGE_SIZE, filters.getPageNum()));

        logger.endDebug(session, resultSet);
        return resultSet;
    }
}
