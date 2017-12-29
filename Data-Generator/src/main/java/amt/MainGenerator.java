package amt;

import am.infrastructure.data.enums.Roles;
import amt.generators.CourseGenerator;
import amt.generators.CoursePRGenerator;
import amt.generators.CourseRefGenerator;
import amt.generators.UserGenerator;
import amt.generic.Gen;
import amt.model.Course;
import amt.model.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static amt.generic.Gen.FULL_SCRIPT_PATH;

/**
 * Created by ahmed.motair on 12/11/2017.
 */
public class MainGenerator {
    public static void main(String[] args) {
        try {
            // Clearing the Full Script File
            PrintWriter out = new PrintWriter(FULL_SCRIPT_PATH);
            out.write(""); out.flush(); out.close();

            String initialScript = "USE amt;\n" +
                    "DELETE FROM amt.course_pre_requisite WHERE pre_requisite_id > 0;\n" +
                    "DELETE FROM amt.course_reference WHERE reference_id > 0;\n" +
                    "DELETE FROM amt.course WHERE created_by > 0;\n" +
                    "DELETE FROM amt.users WHERE user_id > 10;\n";

            Gen.writeToFullScript(initialScript);

            UserGenerator userGenerator = new UserGenerator();
            List<User> userList = userGenerator.generate(100, "2016-10-25", 20);

            List<User> students = new ArrayList<>();
            List<User> tutors = new ArrayList<>();

            for (User user : userList) {
                if (user.getRole().equals(Roles.STUDENT.role()))
                    students.add(user);
                else if (user.getRole().equals(Roles.TUTOR.role()))
                    tutors.add(user);
            }

            CourseGenerator courseGenerator = new CourseGenerator();
            List<Course> courseList = courseGenerator.generate(tutors);

            CoursePRGenerator coursePRGenerator = new CoursePRGenerator();
            coursePRGenerator.generate(courseList);

            CourseRefGenerator courseRefGenerator = new CourseRefGenerator();
            courseRefGenerator.generate(courseList);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
