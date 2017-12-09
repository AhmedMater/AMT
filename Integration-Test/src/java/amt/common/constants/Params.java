package amt.common.constants;

import am.infrastructure.data.dto.user.LoginData;

/**
 * Created by ahmed.motair on 12/7/2017.
 */
public class Params {

    public static final String OWNER_USERNAME = "Owner_User";
    public static final String OWNER_PASSWORD = "123456";
    public static final LoginData ownerLoginData = new LoginData(OWNER_USERNAME, OWNER_PASSWORD);

    public static final String ADMIN_USERNAME = "Admin_User";
    public static final String ADMIN_PASSWORD = "123456";
    public static final LoginData adminLoginData = new LoginData(ADMIN_USERNAME, ADMIN_PASSWORD);

    public static final String TUTOR_USERNAME = "Tutor_User";
    public static final String TUTOR_PASSWORD = "123456";
    public static final LoginData tutorLoginData = new LoginData(TUTOR_USERNAME, TUTOR_PASSWORD);

    public static final String STUDENT_USERNAME = "Student_User";
    public static final String STUDENT_PASSWORD = "123456";
    public static final LoginData studentLoginData = new LoginData(STUDENT_USERNAME, STUDENT_PASSWORD);

}
