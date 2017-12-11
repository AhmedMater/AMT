package amt.generic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmed.motair on 12/10/2017.
 */
public class Gen {
    private static final String MAIN_SCRIPTS_PATH = "F:\\Giza-Projects\\AMT\\AMT-Code\\Data-Generator\\src\\main\\resources\\";
    public static final String FULL_SCRIPT_PATH = "D:\\FullScript.sql";

    public static final String USER_SCRIPT_PATH = MAIN_SCRIPTS_PATH + "UserScript.sql";
    public static final String COURSE_SCRIPT_PATH = MAIN_SCRIPTS_PATH + "CourseScript.sql";
    public static final String COURSE_REF_SCRIPT_PATH = MAIN_SCRIPTS_PATH + "CourseRefScript.sql";
    public static final String COURSE_PR_SCRIPT_PATH = MAIN_SCRIPTS_PATH + "CoursePRScript.sql";


    public static String escapeSingleQuote(String value){
        return value.replaceAll("'", "''");
    }
    public static String formatDate(Date value){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(value);
    }
    public static Date plusDate(Date value, int days, int months, int years) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = sdf.format(value);

        Integer _year = Integer.parseInt(dateStr.substring(0, 4));
        Integer _month = Integer.parseInt(dateStr.substring(5, 7));
        Integer _day = Integer.parseInt(dateStr.substring(8, 10));

        dateStr = (_year + years) + "-" + (_month + months) + "-" + (_day + days) + dateStr.substring(10);

        return sdf.parse(dateStr);
    }

    public static void writeToFullScript(String script){
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FULL_SCRIPT_PATH, true)));
            out.println(script);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
