package amt.generators;

import amt.generic.Gen;
import amt.model.Course;
import amt.model.CourseRef;
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
public class CourseRefGenerator {

    private LoremIpsum loremIpsum = new LoremIpsum();
    private Faker faker = new Faker();
    private Random rand = new Random();

    List<Integer> courseRefIDs = new ArrayList<>();

    private Integer preRefNum = 1;
    private List<String> types = Arrays.asList("Bo", "Co");

    public List<CourseRef> generate(List<Course> courses) throws Exception{
        List<CourseRef> courseRefList = new ArrayList<>();

        for (Course course : courses) {
            int numOfCourseRefs = 1 + rand.nextInt(5);

            for (int i = 0; i < numOfCourseRefs; i++) {
                CourseRef courseRef = new CourseRef();

                Integer id = rand.nextInt(courses.size() * 5 * 20) + 1;

                boolean found = false;

                for (Integer _id : courseRefIDs)
                    if (id.equals(_id))
                        found = true;

                if(found) {
                    i--;
                    continue;
                }

                courseRefIDs.add(id);

                courseRef.setCourseRefID(id);
                courseRef.setCourseID(course.getCourseID());
                courseRef.setNum(i+1);
                courseRef.setType(types.get(rand.nextInt(2)));

                if(courseRef.getType().equals("Bo"))
                    courseRef.setName(faker.book().title());
                else if(courseRef.getType().equals("Co"))
                    courseRef.setName(faker.educator().course());

                courseRef.setUrl(faker.internet().url());

                courseRefList.add(courseRef);
//                System.out.println("Course Reference of ID: " + courseRef.getCourseRefID() + " is created");
            }
        }
        System.out.println(courseRefList.size() + " Course References are created");

        StringBuilder script = new StringBuilder();
        for (CourseRef courseRef : courseRefList)
            script.append(courseRef.generateSQL()).append("\n");

        PrintWriter out = new PrintWriter(Gen.COURSE_REF_SCRIPT_PATH);
        out.print(script.toString());
        out.flush();
        out.close();

        Gen.writeToFullScript(script.toString());
        return courseRefList;
    }
}
