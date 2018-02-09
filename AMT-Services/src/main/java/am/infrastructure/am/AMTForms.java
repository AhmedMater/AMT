package am.infrastructure.am;


import am.main.spi.AMForm;

/**
 * Created by ahmed.motair on 1/31/2018.
 */
public class AMTForms extends AMForm {

    public static final AMTForms REGISTER = new AMTForms("Register");
    public static final AMTForms LOGIN = new AMTForms("Login");
    public static final AMTForms CHANGE_ROLE = new AMTForms("Change Role");
    public static final AMTForms NEW_COURSE = new AMTForms("New Course");
    public static final AMTForms COURSE_LIST_FILTERS = new AMTForms("Course List Filters");
    public static final AMTForms USER_LIST_FILTERS = new AMTForms("User List Filters");

    public static final AMTForms NOTIFICATION_EVENT = new AMTForms("Notification Event");

    private AMTForms(String form) {
        super(form);
    }
}
