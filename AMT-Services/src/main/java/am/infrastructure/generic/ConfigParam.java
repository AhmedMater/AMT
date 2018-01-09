package am.infrastructure.generic;

import am.shared.enums.Source;

import static am.shared.enums.Source.AMT_SERVICES;

/**
 * Created by ahmed.motair on 9/7/2017.
 */
public class ConfigParam {

    public static final String EH_CACHE_LOOKUP_REGION = "AMTLookupRegion";
    public static final String EH_CACHE_READ_WRITE_REGION = "AMTReadWriteRegion";

    public static final String COURSE_NUM_PARAM = "Total Course Number";
    public static final String AUTH_USER = "Authenticated-User";

    public static final Integer MAX_PAGE_SIZE = 10;

    public static final Source SOURCE = AMT_SERVICES;
}
