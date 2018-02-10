package am.infrastructure.am.impl;

import am.main.data.enums.logger.LoggerLevels;
import am.main.spi.AMPhase;

/**
 * Created by ahmed.motair on 1/22/2018.
 */
public class ASP extends AMPhase {
    private static final String GENERIC = "Generic";
    private static final String SECURITY = "Security";
    private static final String COURSE = "Course";
    private static final String ARTICLE = "Article";
    private static final String USER = "User";

    public static final ASP AUTHORIZATION = new ASP(SECURITY, "Authorization");
    public static final ASP URL_LOGGING = new ASP(SECURITY, "URL-Logging");

    public static final ASP COURSE_NEW = new ASP(COURSE, "Course-New");
    public static final ASP COURSE_DETAIL = new ASP(COURSE, "Course-Detail");
    public static final ASP COURSE_UPDATE = new ASP(COURSE, "Course-Update");
    public static final ASP COURSE_LIST = new ASP(COURSE, "Course-List");
    public static final ASP COURSE_QUIZ = new ASP(COURSE, "Course-Quiz");
    public static final ASP COURSE_REVIEW = new ASP(COURSE, "Course-Review");
    public static final ASP COURSE_TRANSLATION = new ASP(COURSE, "Course-Translation");

    public static final ASP ARTICLE_NEW = new ASP(ARTICLE, "Article-New");
    public static final ASP ARTICLE_DETAIL = new ASP(ARTICLE, "Article-Detail");
    public static final ASP ARTICLE_UPDATE = new ASP(ARTICLE, "Article-Update");
    public static final ASP ARTICLE_LIST = new ASP(ARTICLE, "Article-List");
    public static final ASP ARTICLE_REVIEW = new ASP(ARTICLE, "Article-Review");
    public static final ASP ARTICLE_TRANSLATION = new ASP(ARTICLE, "Article-Translation");

    public static final ASP USER_REGISTRATION = new ASP(USER, "User-Registration");
    public static final ASP USER_LOGIN = new ASP(USER, "User-Login");
    public static final ASP USER_LIST = new ASP(USER, "User-List");
    public static final ASP USER_DETAIL = new ASP(USER, "User-Detail");
    public static final ASP USER_UPDATE = new ASP(USER, "User-Update");

    public ASP(String CATEGORY, String NAME) {
        super(CATEGORY, NAME, LoggerLevels.INFO);
    }
}
