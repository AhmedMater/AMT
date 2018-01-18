package am.shared.enums.notification;

/**
 * Created by ahmed.motair on 1/6/2018.
 */
public enum Category {
    USER("User"),
    COURSE("Course"),
    ARTICLE("Article"),
    AM("AM"),
    GENERIC("Generic"),
    SECURITY("Security");

    private String name;
    Category(String name){
        this.name = name;
    }
}
