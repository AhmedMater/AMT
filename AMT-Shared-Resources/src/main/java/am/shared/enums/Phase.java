package am.shared.enums;

import am.shared.enums.notification.Category;

import static am.shared.enums.notification.Category.*;
import static am.shared.enums.LoggerLevels.ST_DEBUG;

/**
 * Created by ahmed.motair on 9/20/2017.
 */
public enum Phase {
    SECURITY_MANAGER(AM, "Security-Manager", true, ST_DEBUG),
    MESSAGE_HANDLER(AM, "Message-Handler", true, ST_DEBUG),
    XML_HANDLER(AM, "XML-Handler", true, ST_DEBUG),
    JMS_MANAGER(AM, "JMS-Manager", true, ST_DEBUG),
    DB_MANAGER(AM, "DB-Manager", true, ST_DEBUG),
    QUERY_BUILDER(AM, "Query-Builder", true, ST_DEBUG),
    CONFIG_MANAGER(AM, "Config-Manager", true, ST_DEBUG),
    APP_LOGGER(AM, "App-Logger", true, ST_DEBUG),

    AM_LOGGING(GENERIC, "AM-Logging", true, ST_DEBUG),
    AM_NOTIFICATION(AM, "AM-Notification", true, ST_DEBUG),

    INTEGRATION_TEST(AM, "Integration-Test", true, ST_DEBUG),

    AUTHORIZATION(SECURITY, "Authorization", true, ST_DEBUG),
    URL_LOGGING(SECURITY, "URL-Logging", true, ST_DEBUG),

    COURSE_NEW(COURSE, "Course-New", true, ST_DEBUG),
    COURSE_DETAIL(COURSE, "Course-Detail", true, ST_DEBUG),
    COURSE_UPDATE(COURSE, "Course-Update", true, ST_DEBUG),
    COURSE_LIST(COURSE, "Course-List", true, ST_DEBUG),
    COURSE_QUIZ(COURSE, "Course-Quiz", true, ST_DEBUG),
    COURSE_REVIEW(COURSE, "Course-Review", true, ST_DEBUG),
    COURSE_TRANSLATION(COURSE, "Course-Translation", true, ST_DEBUG),

    ARTICLE_NEW(ARTICLE, "Article-New", true, ST_DEBUG),
    ARTICLE_DETAIL(ARTICLE, "Article-Detail", true, ST_DEBUG),
    ARTICLE_UPDATE(ARTICLE, "Article-Update", true, ST_DEBUG),
    ARTICLE_LIST(ARTICLE, "Article-List", true, ST_DEBUG),
    ARTICLE_REVIEW(ARTICLE, "Article-Review", true, ST_DEBUG),
    ARTICLE_TRANSLATION(ARTICLE, "Article-Translation", true, ST_DEBUG),

    USER_REGISTRATION(USER, "User-Registration", true, ST_DEBUG),
    USER_LOGIN(USER, "User-Login", true, ST_DEBUG),
    USER_LIST(USER, "User-List", true, ST_DEBUG),
    USER_DETAIL(USER, "User-Detail", true, ST_DEBUG),
    USER_UPDATE(USER, "User-Update", true, ST_DEBUG);

    private Category category;
    private String value;
    private Boolean enabled;
    private LoggerLevels level;

    Phase(Category category, String value, Boolean enabled, LoggerLevels level){
        this.category = category;
        this.value = value;
        this.enabled = enabled;
        this.level = level;
    }

    public String value(){return value;}
    public Category category(){return category;}
    public LoggerLevels level(){return level;}
    public Boolean isEnabled(){return enabled;}
}
