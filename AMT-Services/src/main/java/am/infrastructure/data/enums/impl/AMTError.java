package am.infrastructure.data.enums.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/27/2018.
 */
public class AMTError extends AMCode {
    private static final String COURSE = "COR";
    private static final String USER = "USR";

    public static final AMTError E_USR_1 = new AMTError(1, USER);
    public static final AMTError E_USR_2 = new AMTError(2, USER);
    public static final AMTError E_USR_3 = new AMTError(3, USER);
    public static final AMTError E_USR_4 = new AMTError(4, USER);
    public static final AMTError E_USR_5 = new AMTError(5, USER);
    public static final AMTError E_USR_6 = new AMTError(6, USER);
    public static final AMTError E_USR_7 = new AMTError(7, USER);
    public static final AMTError E_USR_8 = new AMTError(8, USER);
    public static final AMTError E_USR_9 = new AMTError(9, USER);
    public static final AMTError E_USR_10 = new AMTError(10, USER);
    public static final AMTError E_USR_11 = new AMTError(11, USER);
    public static final AMTError E_USR_12 = new AMTError(12, USER);
    public static final AMTError E_USR_13 = new AMTError(13, USER);
    public static final AMTError E_USR_14 = new AMTError(14, USER);
    public static final AMTError E_USR_15 = new AMTError(15, USER);
    public static final AMTError E_USR_16 = new AMTError(16, USER);
    public static final AMTError E_USR_17 = new AMTError(17, USER);
    public static final AMTError E_USR_18 = new AMTError(18, USER);
    public static final AMTError E_USR_19 = new AMTError(19, USER);
    public static final AMTError E_USR_20 = new AMTError(20, USER);
    public static final AMTError E_USR_21 = new AMTError(21, USER);
    public static final AMTError E_USR_22 = new AMTError(22, USER);
    public static final AMTError E_USR_23 = new AMTError(23, USER);
    public static final AMTError E_USR_24 = new AMTError(24, USER);
    public static final AMTError E_USR_25 = new AMTError(25, USER);
    public static final AMTError E_USR_26 = new AMTError(26, USER);
    public static final AMTError E_USR_27 = new AMTError(27, USER);
    public static final AMTError E_USR_28 = new AMTError(28, USER);
    public static final AMTError E_USR_29 = new AMTError(29, USER);
    public static final AMTError E_USR_30 = new AMTError(30, USER);

    public static final AMTError E_COR_1 = new AMTError(1, COURSE);
    public static final AMTError E_COR_2 = new AMTError(2, COURSE);
    public static final AMTError E_COR_3 = new AMTError(3, COURSE);
    public static final AMTError E_COR_4 = new AMTError(4, COURSE);
    public static final AMTError E_COR_5 = new AMTError(5, COURSE);
    public static final AMTError E_COR_6 = new AMTError(6, COURSE);
    public static final AMTError E_COR_7 = new AMTError(7, COURSE);
    public static final AMTError E_COR_8 = new AMTError(8, COURSE);
    public static final AMTError E_COR_9 = new AMTError(9, COURSE);

    private AMTError(Integer CODE_ID, String CODE_NAME) {
        super(CodeTypes.ERROR, false, CODE_ID, CODE_NAME, null, "AMT");
    }
}
