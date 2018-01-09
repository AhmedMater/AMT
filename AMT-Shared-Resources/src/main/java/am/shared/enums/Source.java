package am.shared.enums;

/**
 * Created by mohamed.elewa on 13/05/2017.
 */
public enum Source {
    AM("AM-Library"),
    AMT_SERVICES("AMT-Services"),
    INTEGRATION_TEST("Integration-Test"),
    AMT_LOGGER("AMT-Logger");

    private String value;

    Source(String value){
        this.value = value;
    }
    public String value(){return value;}

}
