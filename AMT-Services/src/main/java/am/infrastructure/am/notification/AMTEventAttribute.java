package am.infrastructure.am.notification;

import am.main.spi.notification.AMEvent;
import am.main.spi.notification.AMEventAttribute;

/**
 * Created by ahmed.motair on 1/31/2018.
 */
public class AMTEventAttribute extends AMEventAttribute {

    public static final AMTEventAttribute USER_FULL_NAME = new AMTEventAttribute(AMTEvent.NEW_COURSE, "UFN");
    public static final AMTEventAttribute COURSE_NAME = new AMTEventAttribute(AMTEvent.NEW_COURSE, "CN");
    public static final AMTEventAttribute COURSE_ID = new AMTEventAttribute(AMTEvent.NEW_COURSE, "CID");
    public static final AMTEventAttribute COURSE_TYPE = new AMTEventAttribute(AMTEvent.NEW_COURSE, "CT");
    public static final AMTEventAttribute COURSE_LEVEL = new AMTEventAttribute(AMTEvent.NEW_COURSE, "CL");
    public static final AMTEventAttribute COURSE_ESTIMATED_DURATION = new AMTEventAttribute(AMTEvent.NEW_COURSE, "CED");

    private AMTEventAttribute(AMEvent event, String attribute) {
        super(event, attribute);
    }
}
