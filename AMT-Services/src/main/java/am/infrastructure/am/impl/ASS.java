package am.infrastructure.am.impl;

import am.main.spi.AMSource;

/**
 * Created by ahmed.motair on 1/27/2018.
 */
public class ASS extends AMSource{

    public static ASS AMT_SERVICES = new ASS("AMT-Services");

    protected ASS(String name) {
        super(name);
    }
}
