package amt.generators;

import am.infrastructure.data.enums.Roles;
import com.github.javafaker.Faker;
import com.thedeanda.lorem.LoremIpsum;
import amt.generic.Gen;
import amt.model.User;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahmed.motair on 12/10/2017.
 */
public class UserGenerator {
    private LoremIpsum loremIpsum = new LoremIpsum();
    private Faker faker = new Faker();
    private Random rand = new Random();

    private Integer tutorNum = 0;
    private List<String> roles = Arrays.asList(Roles.STUDENT.role(), Roles.TUTOR.role());

    public List<User> generate(int userNum, String fromCreationDateStr, Integer tutorPercentage){
        try {
            Date fromCreationDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromCreationDateStr);
            List<Integer> userIDs = new ArrayList<>();

            List<User> userList = new ArrayList<>();
            int i = 0;
            while (i < userNum) {
                User user = new User();
                Integer userID = 100 + rand.nextInt(userNum*20) + 1;

                boolean found = false;

                for (Integer _userID : userIDs)
                    if (userID.equals(_userID))
                        found = true;

                if(found)
                    continue;

                userIDs.add(userID);

                user.setUserID(userID);
                user.setFirstName(loremIpsum.getName());
                user.setLastName(loremIpsum.getName());
                user.setUsername(faker.name().username());
                user.setPassword(faker.name().nameWithMiddle());
                user.setEmail(loremIpsum.getEmail());
                String role = roles.get(rand.nextInt(2));

                if(role.equals("Tu")) {
                    int tutPercentage = (int) ((float) tutorNum / userNum * 100f);
                    if (( tutPercentage < tutorPercentage)) {
                        tutorNum++;
                        user.setRole(role);
                    } else
                        continue;
                }else
                    user.setRole(role);


                user.setCreationDate(faker.date().between(fromCreationDate, new Date()));

                userList.add(user);
//                System.out.println("User num: " + (i+1) + " is created");
                i++;
            }

            System.out.println(userList.size() + " Users are created");

            StringBuilder script = new StringBuilder();
            for (User us : userList)
                script.append(us.generateSQL()).append("\n");

            PrintWriter out = new PrintWriter(Gen.USER_SCRIPT_PATH);
            out.print(script.toString());
            out.flush();
            out.close();

            Gen.writeToFullScript(script.toString());
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
