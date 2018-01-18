package am.shared.enums;

/**
 * Created by ahmed.motair on 11/25/2017.
 */
public enum Forms {
    REGISTER("Register"),
    LOGIN("Login"),
    CHANGE_ROLE("Change Role"),
    NEW_COURSE("New Course"),
    COURSE_LIST_FILTERS("Course List Filters"),
    USER_LIST_FILTERS("User List Filters"),
    NOTIFICATION_EVENT("Notification Event");

    private String name;

    Forms(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
