package am.infrastructure.data.enums.impl;

import am.main.spi.AMSource;

/**
 * Created by ahmed.motair on 1/27/2018.
 */
public class AMTSource extends AMSource{

    public static AMTSource AMT_SERVICES = new AMTSource("AMT-Services");

    protected AMTSource(String name) {
        super(name);
    }
}
