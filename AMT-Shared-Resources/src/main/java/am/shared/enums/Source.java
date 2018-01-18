package am.shared.enums;

/**
 * Created by mohamed.elewa on 13/05/2017.
 */
public enum Source {
    AM("AM-Library"),
    AM_LOGGER("AM-Logger"),
    AM_NOTIFICATION("AM-Notification"),
    AM_FILE_MANAGER("AM-File-Manager"),

    AMT_SERVICES("AMT-Services"),
    INTEGRATION_TEST("Integration-Test");

    private String value;

    Source(String value){
        this.value = value;
    }
    public String value(){return value;}

}
