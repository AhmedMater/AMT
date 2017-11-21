package amt.common.constants;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public class SQL {
    private static final String BASE_PATH = "scripts";

    public static final String CLEARING_SCRIPT = BASE_PATH + "/clearing_script.sql";

    private static final String LOOKUP = BASE_PATH + "/lookup";
    public static final String ROLE = LOOKUP + "/role.sql";
    public static final String COURSE_TYPE = LOOKUP + "/course_type.sql";
    public static final String COURSE_LEVEL = LOOKUP + "/course_level.sql";
    public static final String COURSE_CONTENT = LOOKUP + "/course_content.sql";
    public static final String MATERIAL_TYPE = LOOKUP + "/material_type.sql";
    public static final String DATA_TYPE = LOOKUP + "/data_type.sql";
    public static final String SYSTEM_PARAMETER = LOOKUP + "/system_parameter.sql";

    private static final String USER = BASE_PATH + "/user";
    public static final String NEW_USER = USER + "/insert_new_user.sql";
    public static final String REMOVE_USERS = USER + "/remove_users.sql";

    private static final String COURSE = BASE_PATH + "/course";

}
