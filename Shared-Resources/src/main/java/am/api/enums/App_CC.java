package am.api.enums;

/**
 * Created by ahmed.motair on 9/9/2017.
 */
public enum App_CC {
    MAX_LOGIN_TRAILS("MaxLoginTrails"),
    LOGIN_ACTIVATE_MINUTES("LoginActivateMinutes");

    private String value;

    App_CC(String value){
        this.value = value;
    }
    App_CC(){}
    public String value(){
        return value;
    }
}
