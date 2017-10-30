package am.infrastructure.generic;


import am.api.components.AppLogger;
import am.api.enums.EC;
import am.exception.BusinessException;
import am.session.AppSession;

/**
 * Created by ahmed.motair on 9/7/2017.
 */
public class ConfigUtils {
    private static final String CLASS = "ConfigUtils";

    public static BusinessException businessException(AppLogger logger, AppSession session, Exception ex, EC ec, Object ... args){
        if(ex instanceof BusinessException) {
            logger.error(session, ec, args);
            return (BusinessException) ex;
        } else {
            logger.error(session, ex, ec, args);
            return new BusinessException(session, ex, EC.AMT_0000);
        }
    }

}
