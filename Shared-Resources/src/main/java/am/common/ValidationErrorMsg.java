package am.common;

/**
 * Created by ahmed.motair on 11/15/2017.
 */
public class ValidationErrorMsg extends am.common.validation.ValidationErrorMsg{
    public static final class FIRST_NAME{
        private static final String FIELD_NAME = "First Name";
        
        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 1 to 15 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + REAL_NAME_REGEX_MSG;
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class LAST_NAME{
        private static final String FIELD_NAME = "Last Name";
        
        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 1 to 15 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + REAL_NAME_REGEX_MSG;
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class USERNAME{
        private static final String FIELD_NAME = "Username";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 5 to 50 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + USERNAME_REGEX_MSG;
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class PASSWORD{
        private static final String FIELD_NAME = "Password";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 5 to 30 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + PASSWORD_REGEX_MSG;
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class EMAIL{
        private static final String FIELD_NAME = "Email";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 10 to 100 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + "'";
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class COURSE_NAME{
        private static final String FIELD_NAME = "Course Name";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 10 to 100 char";
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + CONTENT_NAME_REGEX_MSG;
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class COURSE_LEVEL{
        private static final String FIELD_NAME = "Course Level";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_007_ST + FIELD_NAME + AMT_VALID_007_EN + " 2 char";
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class COURSE_TYPE{
        private static final String FIELD_NAME = "Course Type";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String LENGTH = AMT_VALID_007_ST + FIELD_NAME + AMT_VALID_007_EN + " 2 char";
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }
    
    public static final class COURSE_DESCRIPTION{
        private static final String FIELD_NAME = "Course Description";

        public static final String LENGTH = AMT_VALID_006_ST + FIELD_NAME + AMT_VALID_006_EN + " 2 char";
        public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
    }

    public static final class COURSE_DURATION{
        private static final String FIELD_NAME = "Course Duration";

        public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
        public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + POSITIVE_NUM_REGEX_MSG;
    }

    public static final class COURSE_REFERENCE{
        private static final String MAIN_FIELD = "Course Reference";

        public static final class ORDER{
            private static final String FIELD_NAME = MAIN_FIELD + " Order";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + POSITIVE_NUM_REGEX_MSG;
        }

        public static final class NAME{
            private static final String FIELD_NAME = MAIN_FIELD + " Name";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 10 to 100 char";
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + CONTENT_NAME_REGEX_MSG;
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }

        public static final class TYPE{
            private static final String FIELD_NAME = MAIN_FIELD + " Type";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_007_ST + FIELD_NAME + AMT_VALID_007_EN + " 2 char";
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }

        public static final class URL{
            private static final String FIELD_NAME = MAIN_FIELD + " URL";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 5 to 200 char";
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + "'";
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }
    }

    public static final class COURSE_PRE_REQUISITE{
        private static final String MAIN_FIELD = "Course Pre-Requisite";

        public static final class ORDER{
            private static final String FIELD_NAME = MAIN_FIELD + " Order";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + POSITIVE_NUM_REGEX_MSG;
        }

        public static final class NAME{
            private static final String FIELD_NAME = MAIN_FIELD + " Name";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 10 to 100 char";
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + AMT_VALID_003_EN + CONTENT_NAME_REGEX_MSG;
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }

        public static final class TYPE{
            private static final String FIELD_NAME = MAIN_FIELD + " Type";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_007_ST + FIELD_NAME + AMT_VALID_007_EN + " 2 char";
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }

        public static final class URL{
            private static final String FIELD_NAME = MAIN_FIELD + " URL";

            public static final String REQUIRED = AMT_VALID_001_ST + FIELD_NAME + AMT_VALID_001_EN;
            public static final String LENGTH = AMT_VALID_002_ST + FIELD_NAME + AMT_VALID_002_EN + " 5 to 200 char";
            public static final String INVALID = AMT_VALID_003_ST + FIELD_NAME + "'";
            public static final String EMPTY_STR = AMT_VALID_004_ST + FIELD_NAME + AMT_VALID_004_EN;
        }
    }
}
