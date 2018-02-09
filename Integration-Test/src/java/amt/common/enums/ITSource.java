package amt.common.enums;

import am.main.spi.AMSource;

/**
 * Created by ahmed.motair on 1/29/2018.
 */
public class ITSource extends AMSource {
    public static ITSource INTEGRATION_TEST = new ITSource("Integration-Test");
    protected ITSource(String name) {
        super(name);
    }
}
