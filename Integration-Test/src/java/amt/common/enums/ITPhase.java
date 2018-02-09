package amt.common.enums;

import am.main.data.enums.logger.LoggerLevels;
import am.main.spi.AMPhase;

/**
 * Created by ahmed.motair on 1/29/2018.
 */
public class ITPhase extends AMPhase {
    public static final ITPhase IT = new ITPhase("Integration-Test");
    ITPhase(String NAME) {
        super("AMT", NAME, LoggerLevels.EN_DEBUG);
    }
}
