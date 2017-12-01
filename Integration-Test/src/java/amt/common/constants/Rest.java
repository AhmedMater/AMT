package amt.common.constants;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public class Rest {
    public static final String BASE_URL = "http://localhost/AMT-Services/api";

    public static final class COURSE {
        public static final String RESOURCE = BASE_URL + "/course";

        public static final String NEW = "/new";
    }

    public static final class USER {
        public static final String RESOURCE = BASE_URL + "/user";

        public static final String REGISTER = "/register";
        public static final String LOGIN = "/login";
        public static final String CHANGE_ROLE = "/profile/changeRole/{0}";

        public static final class GET_PROFILE {
            public static final String PATH = "/profile/{0}/";
            public static final String QP_VIEWER_ID = "viewerID";
        }
    }
}
