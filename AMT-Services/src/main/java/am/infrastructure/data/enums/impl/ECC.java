package am.infrastructure.data.enums.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/22/2018.
 */
public class ECC extends AMCode{
    public static final ECC MAX_LOGIN_TRAILS = new ECC("MaxLoginTrails");
    public static final ECC LOGIN_ACTIVATION_MIN = new ECC("LoginActivateMinutes");

//    public static final ECC LOG_FILE_SIZE = new ECC("LogFileSize");
//    public static final ECC MAIN_LOG_FILES_PATH = new ECC("MainLogFilesPath");
//    public static final ECC LOGGER_LEVEL = new ECC("LoggerLevel");

    public ECC(String CODE_NAME) {
        super(CodeTypes.CONFIGURATION, false, -1, CODE_NAME, "", "");
    }
}
