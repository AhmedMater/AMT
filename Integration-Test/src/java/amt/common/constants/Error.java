package amt.common.constants;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public class Error {
    public static final String SET_UP = "Error happen while setting up the Test Case due to: {0}";
    public static final String TEST_CASE = "Error happen while executing the Test Case: {0} due to: {1}";

    public static final String NOT_AUTHORIZED = "AMT-0036: You aren't authorized to access this page";
    public static final String LOOKUP_NOT_FOUND = "AMT-0002: Lookup ''{0}'' of ID: ''{1}'' isn''t found in the System";

    public static class FV {
        public static final String REQUIRED         = "AMT-0003: Field: ''{0}'' is a Mandatory field";
        public static final String REGEX            = "AMT-0004: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows only ''{2}''";
        public static final String EMPTY_STR        = "AMT-0005: Field: ''{0}'' is invalid as It doesn''t allow Empty String";
        public static final String MAX_LENGTH       = "AMT-0006: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows max length ''{2}'' char";
        public static final String MIN_LENGTH       = "AMT-0007: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows min length ''{2}'' char";
        public static final String MIN_MAX_LENGTH   = "AMT-0008: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows length of min ''{2}'' to max ''{3}'' char";
        public static final String EQ_LENGTH        = "AMT-0009: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows value of length ''{2}'' char";
        public static final String MAX_VALUE        = "AMT-0010: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows max value ''{2}''";
        public static final String MIN_VALUE        = "AMT-0011: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows min value ''{2}''";
        public static final String FUTURE_DATE      = "AMT-0012: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows only Future Date";
        public static final String POSITIVE_NUM     = "AMT-0013: Value: ''{0}'' is invalid for Field: ''{1}'', as It allows only positive numbers";
    }

    public static class USER {
        public static final String REGISTER_VALIDATION_ERROR = "AMT-0001: Register Form validation failed";
        public static final String LOGIN_VALIDATION_ERROR = "AMT-0001: Login Form validation failed";
        public static final String CHANGE_ROLE_VALIDATION_ERROR = "AMT-0001: Change Role Form validation failed";
        public static final String DUPLICATE_USER = "AMT-0021: Username: ''{0}'' already exists in the System";
        public static final String DUPLICATE_EMAIL = "AMT-0022: Email: ''{0}'' already exists in the System";
        public static final String INVALID_ROLE = "AMT-0026: Invalid Role: ''{0}''";
        public static final String CANT_UPGRADE_TO_ADMIN = "AMT-0027: Owner Role only can assign Admin Roles to Users";
        public static final String USER_IS_NOT_FOUND = "AMT-0034: User: ''{0}'' isn't found in the System";
        public static final String WRONG_PASSWORD = "AMT-0014: Wrong Password, Please try again";
        public static final String WRONG_PASSWORD_GT_MAX_TRAILS = "AMT-0015: Wrong Password, You have tried {0} trails, Your Account will be deactivated for {1} Min";
        public static final String WRONG_PASSWORD_LT_LOGIN_DEACTIVATION_DURATION = "AMT-0016: This User is deactivated, you have to wait {0} more Minutes and try login again";

//        public static final String REQUIRED_USERNAME = "AM-VALID-001: Field 'Username' is mandatory";
//        public static final String LENGTH_USERNAME = "AM-VALID-002: Length of Field 'Username' has to be within 5 to 50 char";
//        public static final String INVALID_USERNAME = "AM-VALID-003: Invalid Field 'Username' as It allows only chars, numbers, period, hyphen, and Underscore";
//        public static final String EMPTY_STR_USERNAME = "AM-VALID-004: Field 'Username' can't be empty";
//
//        public static final String REQUIRED_PASSWORD = "AM-VALID-001: Field 'Password' is mandatory";
//        public static final String LENGTH_PASSWORD = "AM-VALID-002: Length of Field 'Password' has to be within 5 to 30 char";
//        public static final String INVALID_PASSWORD = "AM-VALID-003: Invalid Field 'Password' as It allows only chars, numbers, period, hyphen, Ampersand, and Underscore";
//        public static final String EMPTY_STR_PASSWORD = "AM-VALID-004: Field 'Password' can't be empty";
//
//        public static final String REQUIRED_EMAIL = "AM-VALID-001: Field 'Email' is mandatory";
//        public static final String LENGTH_EMAIL = "AM-VALID-002: Length of Field 'Email' has to be within 10 to 100 char";
//        public static final String INVALID_EMAIL = "AM-VALID-003: Invalid Field 'Email'";
//        public static final String EMPTY_STR_EMAIL = "AM-VALID-004: Field 'Email' can't be empty";
//
//        public static final String REQUIRED_FIRST_NAME = "AM-VALID-001: Field 'First Name' is mandatory";
//        public static final String LENGTH_FIRST_NAME = "AM-VALID-002: Length of Field 'First Name' has to be within 1 to 15 char";
//        public static final String INVALID_FIRST_NAME = "AM-VALID-003: Invalid Field 'First Name' as It allows only chars, hyphen, comma, period, and Apostrophe";
//        public static final String EMPTY_STR_FIRST_NAME = "AM-VALID-004: Field 'First Name' can't be empty";
//
//        public static final String REQUIRED_LAST_NAME = "AM-VALID-001: Field 'Last Name' is mandatory";
//        public static final String LENGTH_LAST_NAME = "AM-VALID-002: Length of Field 'Last Name' has to be within 1 to 15 char";
//        public static final String INVALID_LAST_NAME = "AM-VALID-003: Invalid Field 'Last Name' as It allows only chars, hyphen, comma, period, and Apostrophe";
//        public static final String EMPTY_STR_LAST_NAME = "AM-VALID-004: Field 'Last Name' can't be empty";
//
//        public static final String REQUIRED_OWNER_ID = "AM-VALID-001: Field 'Owner UserID' is mandatory";
//        public static final String INVALID_OWNER_ID = "AM-VALID-003: Invalid Field 'Owner UserID' as It allows only positive numbers";
//
//        public static final String REQUIRED_VIEWER_ID = "AM-VALID-001: Field 'Viewer UserID' is mandatory";
//        public static final String INVALID_VIEWER_ID = "AM-VALID-003: Invalid Field 'Viewer UserID' as It allows only positive numbers";

//        public static final String REQUIRED_ROLE = "AM-VALID-001: Field 'User Role' is mandatory";
//        public static final String LENGTH_ROLE = "AM-VALID-007: Length of Field 'User Role' has to be of 2 char";
//        public static final String EMPTY_STR_ROLE = "AM-VALID-004: Field 'User Role' can't be empty";


    }

    public static class COURSE {
        public static final String NEW_COURSE_VAL = "AMT-0001: New Course Form validation failed";

        public static final String REQUIRED_COURSE_NAME = "AM-VALID-001: Field 'Course Name' is mandatory";
        public static final String LENGTH_COURSE_NAME = "AM-VALID-002: Length of Field 'Course Name' has to be within 5 to 100 char";
        public static final String INVALID_COURSE_NAME = "AM-VALID-003: Invalid Field 'Course Name' as It allows only chars, numbers, hyphen, comma, period, space and Apostrophe";
        public static final String EMPTY_STR_COURSE_NAME = "AM-VALID-004: Field 'Course Name' can't be empty";

        public static final String REQUIRED_COURSE_LEVEL = "AM-VALID-001: Field 'Course Level' is mandatory";
        public static final String LENGTH_COURSE_LEVEL = "AM-VALID-007: Length of Field 'Course Level' has to be of 2 char";
        public static final String EMPTY_STR_COURSE_LEVEL = "AM-VALID-004: Field 'Course Level' can't be empty";
        public static final String INVALID_COURSE_LEVEL = "AM-VALID-003: Invalid Field 'Course Level' as It allows only chars";

        public static final String REQUIRED_COURSE_TYPE = "AM-VALID-001: Field 'Course Type' is mandatory";
        public static final String LENGTH_COURSE_TYPE = "AM-VALID-007: Length of Field 'Course Type' has to be of 2 char";
        public static final String EMPTY_STR_COURSE_TYPE = "AM-VALID-004: Field 'Course Type' can't be empty";
        public static final String INVALID_COURSE_TYPE = "AM-VALID-003: Invalid Field 'Course Type' as It allows only chars";

        public static final String LENGTH_COURSE_DESCRIPTION = "AM-VALID-006: Length of Field 'Course Description' has to be of Max 200 char";
        public static final String EMPTY_STR_COURSE_DESCRIPTION = "AM-VALID-004: Field 'Course Description' can't be empty";

        public static final String REQUIRED_COURSE_DURATION = "AM-VALID-001: Field 'Course Duration' is mandatory";
        public static final String INVALID_COURSE_DURATION = "AM-VALID-003: Invalid Field 'Course Duration' as It allows only positive numbers";
        public static final String MIN_VALUE_COURSE_DURATION = "AM-VALID-008: Value of Field 'Course Duration' has to be of Minimum 5 Hours";

        public static final String REQUIRED_MIN_PER_DAY = "AM-VALID-001: Field 'Course Minutes-Per-Day' is mandatory";
        public static final String INVALID_MIN_PER_DAY = "AM-VALID-003: Invalid Field 'Course Minutes-Per-Day' as It allows only positive numbers";
        public static final String MIN_VALUE_MIN_PER_DAY = "AM-VALID-008: Value of Field 'Course Minutes-Per-Day' has to be of Minimum 10 Minutes per Day";

//        public static final String REQUIRED_START_DATE = "AM-VALID-001: Field 'Course Start Date' is mandatory";
//        public static final String FUTURE_START_DATE = "AM-VALID-009: Value of Field 'Course Start Date' has to be in the Future";

        public static final String REQUIRED_COURSE_PRE_REQUISITE_ORDER = "AM-VALID-001: Field 'Course Pre-Requisite Order' is mandatory";
        public static final String MIN_VALUE_COURSE_PRE_REQUISITE_ORDER = "AM-VALID-008: Value of Field 'Course Pre-Requisite Order' has to be of Minimum 1";
        public static final String INVALID_COURSE_PRE_REQUISITE_ORDER = "AM-VALID-003: Invalid Field 'Course Pre-Requisite Order' as It allows only positive numbers";

        public static final String REQUIRED_COURSE_PRE_REQUISITE_NAME = "AM-VALID-001: Field 'Course Pre-Requisite Name' is mandatory";
        public static final String LENGTH_COURSE_PRE_REQUISITE_NAME = "AM-VALID-002: Length of Field 'Course Pre-Requisite Name' has to be within 5 to 100 char";
        public static final String INVALID_COURSE_PRE_REQUISITE_NAME = "AM-VALID-003: Invalid Field 'Course Pre-Requisite Name' as It allows only chars, numbers, hyphen, comma, period, space and Apostrophe";
        public static final String EMPTY_STR_COURSE_PRE_REQUISITE_NAME = "AM-VALID-004: Field 'Course Pre-Requisite Name' can't be empty";

        public static final String REQUIRED_COURSE_PRE_REQUISITE_TYPE = "AM-VALID-001: Field 'Course Pre-Requisite Type' is mandatory";
        public static final String LENGTH_COURSE_PRE_REQUISITE_TYPE = "AM-VALID-007: Length of Field 'Course Pre-Requisite Type' has to be of 2 char";
        public static final String INVALID_COURSE_PRE_REQUISITE_TYPE = "AM-VALID-003: Invalid Field 'Course Pre-Requisite Type' as It allows only chars";
        public static final String EMPTY_STR_COURSE_PRE_REQUISITE_TYPE = "AM-VALID-004: Field 'Course Pre-Requisite Type' can't be empty";

        public static final String REQUIRED_COURSE_PRE_REQUISITE_URL = "AM-VALID-001: Field 'Course Pre-Requisite URL' is mandatory";
        public static final String LENGTH_COURSE_PRE_REQUISITE_URL = "AM-VALID-002: Length of Field 'Course Pre-Requisite URL' has to be within 5 to 200 char";
        public static final String INVALID_COURSE_PRE_REQUISITE_URL = "AM-VALID-003: Invalid Field 'Course Pre-Requisite URL'";
        public static final String EMPTY_STR_COURSE_PRE_REQUISITE_URL = "AM-VALID-004: Field 'Course Pre-Requisite URL' can't be empty";

        public static final String REQUIRED_COURSE_REFERENCE_ORDER = "AM-VALID-001: Field 'Course Reference Order' is mandatory";
        public static final String MIN_VALUE_COURSE_REFERENCE_ORDER = "AM-VALID-008: Value of Field 'Course Reference Order' has to be of Minimum 1";
        public static final String INVALID_COURSE_REFERENCE_ORDER = "AM-VALID-003: Invalid Field 'Course Reference Order' as It allows only positive numbers";

        public static final String REQUIRED_COURSE_REFERENCE_NAME = "AM-VALID-001: Field 'Course Reference Name' is mandatory";
        public static final String LENGTH_COURSE_REFERENCE_NAME = "AM-VALID-002: Length of Field 'Course Reference Name' has to be within 5 to 100 char";
        public static final String INVALID_COURSE_REFERENCE_NAME = "AM-VALID-003: Invalid Field 'Course Reference Name' as It allows only chars, numbers, hyphen, comma, period, space and Apostrophe";
        public static final String EMPTY_STR_COURSE_REFERENCE_NAME = "AM-VALID-004: Field 'Course Reference Name' can't be empty";

        public static final String REQUIRED_COURSE_REFERENCE_TYPE = "AM-VALID-001: Field 'Course Reference Type' is mandatory";
        public static final String LENGTH_COURSE_REFERENCE_TYPE = "AM-VALID-007: Length of Field 'Course Reference Type' has to be of 2 char";
        public static final String INVALID_COURSE_REFERENCE_TYPE = "AM-VALID-003: Invalid Field 'Course Reference Type' as It allows only chars";
        public static final String EMPTY_STR_COURSE_REFERENCE_TYPE = "AM-VALID-004: Field 'Course Reference Type' can't be empty";

        public static final String REQUIRED_COURSE_REFERENCE_URL = "AM-VALID-001: Field 'Course Reference URL' is mandatory";
        public static final String LENGTH_COURSE_REFERENCE_URL = "AM-VALID-002: Length of Field 'Course Reference URL' has to be within 5 to 200 char";
        public static final String INVALID_COURSE_REFERENCE_URL = "AM-VALID-003: Invalid Field 'Course Reference URL'";
        public static final String EMPTY_STR_COURSE_REFERENCE_URL = "AM-VALID-004: Field 'Course Reference URL' can't be empty";


    }
}
