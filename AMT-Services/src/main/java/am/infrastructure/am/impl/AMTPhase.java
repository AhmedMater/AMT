package am.infrastructure.am.impl;

import am.main.data.enums.logger.LoggerLevels;
import am.main.spi.AMPhase;

/**
 * Created by ahmed.motair on 1/22/2018.
 */
public class AMTPhase extends AMPhase {
    private static final String GENERIC = "Generic";
    private static final String SECURITY = "Security";
    private static final String COURSE = "Course";
    private static final String ARTICLE = "Article";
    private static final String USER = "User";

    public static final AMTPhase AUTHORIZATION = new AMTPhase(SECURITY, "Authorization");
    public static final AMTPhase URL_LOGGING = new AMTPhase(SECURITY, "URL-Logging");

    public static final AMTPhase COURSE_NEW = new AMTPhase(COURSE, "Course-New");
    public static final AMTPhase COURSE_DETAIL = new AMTPhase(COURSE, "Course-Detail");
    public static final AMTPhase COURSE_UPDATE = new AMTPhase(COURSE, "Course-Update");
    public static final AMTPhase COURSE_LIST = new AMTPhase(COURSE, "Course-List");
    public static final AMTPhase COURSE_QUIZ = new AMTPhase(COURSE, "Course-Quiz");
    public static final AMTPhase COURSE_REVIEW = new AMTPhase(COURSE, "Course-Review");
    public static final AMTPhase COURSE_TRANSLATION = new AMTPhase(COURSE, "Course-Translation");

    public static final AMTPhase ARTICLE_NEW = new AMTPhase(ARTICLE, "Article-New");
    public static final AMTPhase ARTICLE_DETAIL = new AMTPhase(ARTICLE, "Article-Detail");
    public static final AMTPhase ARTICLE_UPDATE = new AMTPhase(ARTICLE, "Article-Update");
    public static final AMTPhase ARTICLE_LIST = new AMTPhase(ARTICLE, "Article-List");
    public static final AMTPhase ARTICLE_REVIEW = new AMTPhase(ARTICLE, "Article-Review");
    public static final AMTPhase ARTICLE_TRANSLATION = new AMTPhase(ARTICLE, "Article-Translation");

    public static final AMTPhase USER_REGISTRATION = new AMTPhase(USER, "User-Registration");
    public static final AMTPhase USER_LOGIN = new AMTPhase(USER, "User-Login");
    public static final AMTPhase USER_LIST = new AMTPhase(USER, "User-List");
    public static final AMTPhase USER_DETAIL = new AMTPhase(USER, "User-Detail");
    public static final AMTPhase USER_UPDATE = new AMTPhase(USER, "User-Update");

    public AMTPhase(String CATEGORY, String NAME) {
        super(CATEGORY, NAME, LoggerLevels.INFO);
    }
}
