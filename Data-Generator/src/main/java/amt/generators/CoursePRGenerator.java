package amt.generators;

import amt.generic.Gen;
import amt.model.Course;
import amt.model.CoursePR;
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
public class CoursePRGenerator {

    private LoremIpsum loremIpsum = new LoremIpsum();
    private Faker faker = new Faker();
    private Random rand = new Random();

    List<Integer> coursePRIDs = new ArrayList<>();

    private Integer preRequisiteNum = 1;
    private List<String> types = Arrays.asList("Bo", "Co");

    public List<CoursePR> generate(List<Course> courses) throws Exception{
        List<CoursePR> coursePRList = new ArrayList<>();

        for (Course course : courses) {
            int numOfCoursePRs = 1 + rand.nextInt(5);

            for (int i = 0; i < numOfCoursePRs; i++) {
                CoursePR coursePR = new CoursePR();

                Integer id = rand.nextInt(courses.size() * 5 * 20) + 1;

                boolean found = false;

                for (Integer _id : coursePRIDs)
                    if (id.equals(_id)) {
                        found = true;
                        break;
                    }

                if(found) {
                    i--;
                    continue;
                }

                coursePRIDs.add(id);

                coursePR.setCoursePRID(id);
                coursePR.setCourseID(course.getCourseID());
                coursePR.setNum(i+1);
                coursePR.setType(types.get(rand.nextInt(2)));

                if(coursePR.getType().equals("Bo"))
                    coursePR.setName(faker.book().title());
                else if(coursePR.getType().equals("Co"))
                    coursePR.setName(faker.educator().course());

                coursePR.setUrl(faker.internet().url());

                coursePRList.add(coursePR);
//                System.out.println("Course Pre-Requisite of ID: " + coursePR.getCoursePRID() + " is created");
            }
        }
        System.out.println(coursePRList.size() + " Course Pre-Requisites are created");

        StringBuilder script = new StringBuilder();
        for (CoursePR coursePR : coursePRList)
            script.append(coursePR.generateSQL()).append("\n");

        PrintWriter out = new PrintWriter(Gen.COURSE_PR_SCRIPT_PATH);
        out.print(script.toString());
        out.flush();
        out.close();

        Gen.writeToFullScript(script.toString());
        return coursePRList;
    }
}
