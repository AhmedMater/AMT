package am.shared.enums;

/**
 * Created by ahmed.motair on 1/6/2018.
 */
public enum LoggerLevels {
    ST_DEBUG("SD", "debug"),
    EN_DEBUG("ED", "debug"),
    INFO("I", "info"),
    WARN("W", "warn"),
    ERROR_EX("EE", "error"),
    ERROR_MSG("EM", "error"),
    ERROR_MSG_EX("EEM", "error");

    private final String abbreviation;
    private final String level;
    LoggerLevels(String abbreviation, String level){
        this.abbreviation = abbreviation;
        this.level = level;
    }

    public String level(){
        return this.level;
    }
    public String abbreviation(){
        return this.abbreviation;
    }
}
