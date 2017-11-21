package amt.common.constants;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public class Error {
    public static final String SET_UP = "Error happen while setting up the Test Case due to: {0}";
    public static final String TEST_CASE = "Error happen while executing the Test Case: {0} due to: {1}";

    public static class USER {
        public static final String REGISTER_VALIDATION_ERROR = "AMT-0021: User Register validation failed";
        public static final String LOGIN_VALIDATION_ERROR = "AMT-0022: User Login validation failed";
        public static final String DUPLICATE_USER = "AMT-0001: Username: ''{0}'' already exists in the System";
        public static final String DUPLICATE_EMAIL = "AMT-0002: Email: ''{0}'' already exists in the System";

        public static final String REQUIRED_USERNAME = "AM-VALID-001: Field 'Username' is mandatory";
        public static final String LENGTH_USERNAME = "AM-VALID-002: Length of Field 'Username' has to be within 5 to 50 char";
        public static final String INVALID_USERNAME = "AM-VALID-003: Invalid Field 'Username' as It allows only chars, numbers, period, hyphen, and Underscore";
        public static final String EMPTY_STR_USERNAME = "AM-VALID-004: Field 'Username' can't be empty";

        public static final String REQUIRED_PASSWORD = "AM-VALID-001: Field 'Password' is mandatory";
        public static final String LENGTH_PASSWORD = "AM-VALID-002: Length of Field 'Password' has to be within 5 to 30 char";
        public static final String INVALID_PASSWORD = "AM-VALID-003: Invalid Field 'Password' as It allows only chars, numbers, period, hyphen, Ampersand, and Underscore";
        public static final String EMPTY_STR_PASSWORD = "AM-VALID-004: Field 'Password' can't be empty";

        public static final String REQUIRED_EMAIL = "AM-VALID-001: Field 'Email' is mandatory";
        public static final String LENGTH_EMAIL = "AM-VALID-002: Length of Field 'Email' has to be within 10 to 100 char";
        public static final String INVALID_EMAIL = "AM-VALID-003: Invalid Field 'Email'";
        public static final String EMPTY_STR_EMAIL = "AM-VALID-004: Field 'Email' can't be empty";

        public static final String REQUIRED_FIRST_NAME = "AM-VALID-001: Field 'First Name' is mandatory";
        public static final String LENGTH_FIRST_NAME = "AM-VALID-002: Length of Field 'First Name' has to be within 1 to 15 char";
        public static final String INVALID_FIRST_NAME = "AM-VALID-003: Invalid Field 'First Name' as It allows only chars, hyphen, comma, period, and Apostrophe";
        public static final String EMPTY_STR_FIRST_NAME = "AM-VALID-004: Field 'First Name' can't be empty";

        public static final String REQUIRED_LAST_NAME = "AM-VALID-001: Field 'Last Name' is mandatory";
        public static final String LENGTH_LAST_NAME = "AM-VALID-002: Length of Field 'Last Name' has to be within 1 to 15 char";
        public static final String INVALID_LAST_NAME = "AM-VALID-003: Invalid Field 'Last Name' as It allows only chars, hyphen, comma, period, and Apostrophe";
        public static final String EMPTY_STR_LAST_NAME = "AM-VALID-004: Field 'Last Name' can't be empty";

    }
}
