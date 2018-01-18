package am.shared.enums.notification;

/**
 * Created by ahmed.motair on 1/14/2018.
 */
public enum Attributes {
    USER_FULL_NAME("UserFullName"),
    COURSE_NAME("CourseName"),
    COURSE_ID("CourseID"),
    COURSE_TYPE("CourseType"),
    COURSE_LEVEL("CourseLevel"),
    COURSE_ESTIMATED_DURATION("CourseEstimatedDuration");
    private final String attribute;

    Attributes(String attribute){
        this.attribute = attribute;
    }

    public String attribute() {
        return attribute;
    }
}
