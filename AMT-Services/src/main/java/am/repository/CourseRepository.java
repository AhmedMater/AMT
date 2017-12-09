package am.repository;

import am.infrastructure.data.hibernate.model.SystemParameter;
import am.infrastructure.data.hibernate.model.course.Course;
import am.infrastructure.generic.ConfigParam;
import am.main.api.AppLogger;
import am.main.api.db.DBManager;
import am.main.session.AppSession;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
