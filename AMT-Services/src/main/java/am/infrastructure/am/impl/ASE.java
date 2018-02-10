package am.infrastructure.am.impl;

import am.main.data.enums.CodeTypes;
import am.main.spi.AMCode;

/**
 * Created by ahmed.motair on 1/27/2018.
 */
public class ASE extends AMCode {
    private static final String COURSE = "COR";
    private static final String USER = "USR";

    public static final ASE E_USR_1 = new ASE(1, USER), E_USR_2 = new ASE(2, USER),
        E_USR_3 = new ASE(3, USER),     E_USR_4 = new ASE(4, USER),
        E_USR_5 = new ASE(5, USER),     E_USR_6 = new ASE(6, USER),
        E_USR_7 = new ASE(7, USER),     E_USR_8 = new ASE(8, USER),
        E_USR_9 = new ASE(9, USER),     E_USR_10 = new ASE(10, USER),
        E_USR_11 = new ASE(11, USER),   E_USR_12 = new ASE(12, USER),
        E_USR_13 = new ASE(13, USER),   E_USR_14 = new ASE(14, USER),
        E_USR_15 = new ASE(15, USER),   E_USR_16 = new ASE(16, USER),
        E_USR_17 = new ASE(17, USER),   E_USR_18 = new ASE(18, USER),
        E_USR_19 = new ASE(19, USER),   E_USR_20 = new ASE(20, USER),
        E_USR_21 = new ASE(21, USER),   E_USR_22 = new ASE(22, USER),
        E_USR_23 = new ASE(23, USER),   E_USR_24 = new ASE(24, USER),
        E_USR_25 = new ASE(25, USER),   E_USR_26 = new ASE(26, USER),
        E_USR_27 = new ASE(27, USER),   E_USR_28 = new ASE(28, USER),
        E_USR_29 = new ASE(29, USER),   E_USR_30 = new ASE(30, USER);

    public static final ASE E_COR_1 = new ASE(1, COURSE),
        E_COR_2 = new ASE(2, COURSE),   E_COR_3 = new ASE(3, COURSE),
        E_COR_4 = new ASE(4, COURSE),   E_COR_5 = new ASE(5, COURSE),
        E_COR_6 = new ASE(6, COURSE),   E_COR_7 = new ASE(7, COURSE),
        E_COR_8 = new ASE(8, COURSE),   E_COR_9 = new ASE(9, COURSE);

    private ASE(Integer CODE_ID, String CODE_NAME) {
        super(CodeTypes.ERROR, false, CODE_ID, CODE_NAME, null, "AMT");
    }
}
