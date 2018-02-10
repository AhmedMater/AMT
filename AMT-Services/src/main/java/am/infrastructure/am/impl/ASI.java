package am.infrastructure.am.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/29/2018.
 */
public class ASI extends AMCode{
    private static final String COURSE = "COR";
    private static final String USER = "USR";

    public static final ASI I_USR_1 = new ASI(1, USER);
    public static final ASI I_USR_2 = new ASI(2, USER);
    public static final ASI I_USR_3 = new ASI(3, USER);
    public static final ASI I_USR_4 = new ASI(4, USER);
    public static final ASI I_USR_5 = new ASI(5, USER);
    public static final ASI I_USR_6 = new ASI(6, USER);
    public static final ASI I_USR_7 = new ASI(7, USER);
    public static final ASI I_USR_8 = new ASI(8, USER);
    public static final ASI I_USR_9 = new ASI(9, USER);
    public static final ASI I_USR_10 = new ASI(10, USER);
    public static final ASI I_USR_11 = new ASI(11, USER);
    public static final ASI I_USR_12 = new ASI(12, USER);
    public static final ASI I_USR_13 = new ASI(13, USER);
    public static final ASI I_USR_14 = new ASI(14, USER);
    public static final ASI I_USR_15 = new ASI(15, USER);
    public static final ASI I_USR_16 = new ASI(16, USER);
    public static final ASI I_USR_17 = new ASI(17, USER);
    public static final ASI I_USR_18 = new ASI(18, USER);
    public static final ASI I_USR_19 = new ASI(19, USER);
    public static final ASI I_USR_20 = new ASI(20, USER);
    public static final ASI I_USR_21 = new ASI(21, USER);
    public static final ASI I_USR_22 = new ASI(22, USER);
    public static final ASI I_USR_23 = new ASI(23, USER);
    public static final ASI I_USR_24 = new ASI(24, USER);
    public static final ASI I_USR_25 = new ASI(25, USER);
    public static final ASI I_USR_26 = new ASI(26, USER);
    public static final ASI I_USR_27 = new ASI(27, USER);
    public static final ASI I_USR_28 = new ASI(28, USER);
    public static final ASI I_USR_29 = new ASI(29, USER);
    public static final ASI I_USR_30 = new ASI(30, USER);

    public static final ASI I_COR_1 = new ASI(1, COURSE);
    public static final ASI I_COR_2 = new ASI(2, COURSE);
    public static final ASI I_COR_3 = new ASI(3, COURSE);
    public static final ASI I_COR_4 = new ASI(4, COURSE);
    public static final ASI I_COR_5 = new ASI(5, COURSE);
    public static final ASI I_COR_6 = new ASI(6, COURSE);
    public static final ASI I_COR_7 = new ASI(7, COURSE);
    public static final ASI I_COR_8 = new ASI(8, COURSE);
    public static final ASI I_COR_9 = new ASI(9, COURSE);

    private ASI(Integer CODI_ID, String CODI_NAME) {
        super(CodeTypes.INFO, false, CODI_ID, CODI_NAME, null, "AMT");
    }
}
