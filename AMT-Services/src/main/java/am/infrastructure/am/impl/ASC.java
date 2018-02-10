package am.infrastructure.am.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/22/2018.
 */
public class ASC extends AMCode{
    public static final ASC MAX_LOGIN_TRAILS = new ASC("MaxLoginTrails");
    public static final ASC LOGIN_ACTIVATION_MIN = new ASC("LoginActivateMinutes");

//    public static final ASC LOG_FILE_SIZE = new ASC("LogFileSize");
//    public static final ASC MAIN_LOG_FILES_PATH = new ASC("MainLogFilesPath");
//    public static final ASC LOGGER_LEVEL = new ASC("LoggerLevel");

    public ASC(String CODE_NAME) {
        super(CodeTypes.CONFIGURATION, false, -1, CODE_NAME, "", "");
    }
}
