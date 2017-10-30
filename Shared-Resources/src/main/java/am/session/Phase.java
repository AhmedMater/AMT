package am.session;

/**
 * Created by ahmed.motair on 9/20/2017.
 */
public enum Phase {
    DATABASE("Database"),
    ERROR("ErrorHandler"),
    INFO("InfoHandler"),
    VALIDATION("Validation"),
    EMAIL_NOTIFICATION("Email-Notification"),
    SECURITY("Security"),
    APP_CONFIG("App-Config"),
    AM_CONFIG("AM-Config"),

    REGISTRATION("Registration"),
    LOGIN("Login"),
    AUTHENTICATION("Authentication"),
    AUTHORIZATION("Authorization"),
    URL_LOGGING("URLLogging");

    private String value;

    Phase(String value){
        this.value = value;
    }
    public String value(){return value;}

}
