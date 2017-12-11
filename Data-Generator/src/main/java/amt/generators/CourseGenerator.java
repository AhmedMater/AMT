package amt.generators;

import amt.generic.Gen;
import amt.model.Course;
import amt.model.User;
import com.github.javafaker.Faker;
import com.thedeanda.lorem.LoremIpsum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by ahmed.motair on 12/11/2017.
 */
public class CourseGenerator {

    private LoremIpsum loremIpsum = new LoremIpsum();
    private Faker faker = new Faker();
    private Random rand = new Random();

    private Integer generatedCoursesNum = 1;

    private List<String> levels = Arrays.asList("Be", "In", "Ad");
    private List<String> types = Arrays.asList("Ac", "Pr");

    public List<Course> generate(List<User> tutors) throws Exception{
        List<Course> courses = new ArrayList<>();

        for (User tutor : tutors) {
            int numOfCourses = rand.nextInt(5);

            for (int i = 0; i < numOfCourses; i++) {
                Course course = new Course();
                course.setCourseName(faker.educator().course());
                course.setCourseLevel(levels.get(rand.nextInt(3)));
                course.setCourseType(types.get(rand.nextInt(2)));
                course.setCourseStatus("Fu");
                course.setProgress(0f);

                course.setCreatedBy(tutor.getUserID());
                course.setCreatedOn(Gen.plusDate(tutor.getCreationDate(), rand.nextInt(5), 0, 0));
                course.setEstimatedDuration(5 + rand.nextInt(95));
                course.setEstimatedMinPerDay(5 + rand.nextInt(10));
                course.setDescription(loremIpsum.getParagraphs(1, 20));
                course.setCourseID(generatedCoursesNum);

                generatedCoursesNum++;
                courses.add(course);
//                System.out.println("Course of ID: " + course.getCourseID() + " is created");
            }
        }
        System.out.println(courses.size() + " Courses are created");

        StringBuilder script = new StringBuilder();
        for (Course course : courses)
            script.append(course.generateSQL()).append("\n");

        PrintWriter out = new PrintWriter(Gen.COURSE_SCRIPT_PATH);
        out.print(script.toString());
        out.flush();
        out.close();

        Gen.writeToFullScript(script.toString());
        return courses;
    }

}
