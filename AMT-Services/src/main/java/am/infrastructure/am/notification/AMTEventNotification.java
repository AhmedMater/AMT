package am.infrastructure.am.notification;

import am.main.spi.notification.AMEvent;
import am.main.spi.notification.AMEventNotification;

/**
 * Created by ahmed.motair on 1/31/2018.
 */
public class AMTEventNotification extends AMEventNotification{
    public static final AMEventNotification WELCOME_NOTIFICATION = new AMTEventNotification(AMTEvent.NEW_USER, "WN");
    public static final AMEventNotification HOW_TO_WEBSITE = new AMTEventNotification(AMTEvent.NEW_USER, "HTUW");
    public static final AMEventNotification TOP_ARTICLES = new AMTEventNotification(AMTEvent.NEW_USER, "TA");
    public static final AMEventNotification TOP_COURSES = new AMTEventNotification(AMTEvent.NEW_USER, "TC");

    public static final AMEventNotification INTERESTED_STUDENT = new AMTEventNotification(AMTEvent.NEW_COURSE, "IS");
    public static final AMEventNotification INTERESTED_TECH_REVIEWER = new AMTEventNotification(AMTEvent.NEW_COURSE, "ITR");
    public static final AMEventNotification INTERESTED_TRANSLATOR = new AMTEventNotification(AMTEvent.NEW_COURSE, "IT");

    public AMTEventNotification(AMEvent event, String notification) {
        super(event, notification);
    }
}
