package am.infrastructure.generic;


import am.main.api.AppLogger;
import am.main.exception.BusinessException;
import am.main.session.AppSession;
import am.shared.enums.EC;

/**
 * Created by ahmed.motair on 9/7/2017.
 */
public class ConfigUtils {
    private static final String CLASS = "ConfigUtils";

    public static BusinessException businessException(AppLogger logger, AppSession session, Exception ex, EC ec, Object ... args) {
        if (ex instanceof BusinessException) {
            logger.error(session, ec, args);
            return (BusinessException) ex;
        }
//        else if(ex instanceof ConstraintViolationException){
//            ConstraintViolationException exc = (ConstraintViolationException) ex;
//            Set<ConstraintViolation<?>> violationSet = exc.getConstraintViolations();
//
//            List<String> errors = new ArrayList<>();
//            for (ConstraintViolation<?> violation : violationSet)
//                errors.add(violation.getMessage());
//
//            throw new BusinessException(session, EC.AMT_0021, errors.toString());
//        }
        else {
            logger.error(session, ex, ec, args);
            return new BusinessException(session, ex, EC.AMT_0000);
        }
    }

}
