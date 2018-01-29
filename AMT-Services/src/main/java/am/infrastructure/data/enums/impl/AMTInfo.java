package am.infrastructure.data.enums.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/29/2018.
 */
public class AMTInfo extends AMCode{
    private static final String COURSE = "COR";
    private static final String USER = "USR";

    public static final AMTInfo I_USR_1 = new AMTInfo(1, USER);
    public static final AMTInfo I_USR_2 = new AMTInfo(2, USER);
    public static final AMTInfo I_USR_3 = new AMTInfo(3, USER);
    public static final AMTInfo I_USR_4 = new AMTInfo(4, USER);
    public static final AMTInfo I_USR_5 = new AMTInfo(5, USER);
    public static final AMTInfo I_USR_6 = new AMTInfo(6, USER);
    public static final AMTInfo I_USR_7 = new AMTInfo(7, USER);
    public static final AMTInfo I_USR_8 = new AMTInfo(8, USER);
    public static final AMTInfo I_USR_9 = new AMTInfo(9, USER);
    public static final AMTInfo I_USR_10 = new AMTInfo(10, USER);
    public static final AMTInfo I_USR_11 = new AMTInfo(11, USER);
    public static final AMTInfo I_USR_12 = new AMTInfo(12, USER);
    public static final AMTInfo I_USR_13 = new AMTInfo(13, USER);
    public static final AMTInfo I_USR_14 = new AMTInfo(14, USER);
    public static final AMTInfo I_USR_15 = new AMTInfo(15, USER);
    public static final AMTInfo I_USR_16 = new AMTInfo(16, USER);
    public static final AMTInfo I_USR_17 = new AMTInfo(17, USER);
    public static final AMTInfo I_USR_18 = new AMTInfo(18, USER);
    public static final AMTInfo I_USR_19 = new AMTInfo(19, USER);
    public static final AMTInfo I_USR_20 = new AMTInfo(20, USER);
    public static final AMTInfo I_USR_21 = new AMTInfo(21, USER);
    public static final AMTInfo I_USR_22 = new AMTInfo(22, USER);
    public static final AMTInfo I_USR_23 = new AMTInfo(23, USER);
    public static final AMTInfo I_USR_24 = new AMTInfo(24, USER);
    public static final AMTInfo I_USR_25 = new AMTInfo(25, USER);
    public static final AMTInfo I_USR_26 = new AMTInfo(26, USER);
    public static final AMTInfo I_USR_27 = new AMTInfo(27, USER);
    public static final AMTInfo I_USR_28 = new AMTInfo(28, USER);
    public static final AMTInfo I_USR_29 = new AMTInfo(29, USER);
    public static final AMTInfo I_USR_30 = new AMTInfo(30, USER);

    public static final AMTInfo I_COR_1 = new AMTInfo(1, COURSE);
    public static final AMTInfo I_COR_2 = new AMTInfo(2, COURSE);
    public static final AMTInfo I_COR_3 = new AMTInfo(3, COURSE);
    public static final AMTInfo I_COR_4 = new AMTInfo(4, COURSE);
    public static final AMTInfo I_COR_5 = new AMTInfo(5, COURSE);
    public static final AMTInfo I_COR_6 = new AMTInfo(6, COURSE);
    public static final AMTInfo I_COR_7 = new AMTInfo(7, COURSE);
    public static final AMTInfo I_COR_8 = new AMTInfo(8, COURSE);
    public static final AMTInfo I_COR_9 = new AMTInfo(9, COURSE);

    private AMTInfo(Integer CODI_ID, String CODI_NAME) {
        super(CodeTypes.INFO, false, CODI_ID, CODI_NAME, null, "AMT");
    }
}
