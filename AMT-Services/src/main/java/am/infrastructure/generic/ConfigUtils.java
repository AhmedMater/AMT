package am.infrastructure.generic;


import am.main.api.AppLogger;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.main.spi.AMCode;

import static am.main.data.enums.impl.IEC.E_SYS_0;

/**
 * Created by ahmed.motair on 9/7/2017.
 */
public class ConfigUtils {
    private static final String CLASS = "ConfigUtils";

    public static BusinessException businessException(AppLogger logger, AppSession session, Exception ex, AMCode ec, Object ... args) {
        if (ex instanceof BusinessException) {
            logger.error(session, ex, ec, args);
            return (BusinessException) ex;
        }else {
            logger.error(session, ex, ec, args);
            return new BusinessException(session, E_SYS_0);
        }
    }

}
