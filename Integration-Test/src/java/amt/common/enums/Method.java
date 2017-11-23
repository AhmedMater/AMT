package amt.common.enums;

/**
 * Created by ahmed.motair on 11/19/2017.
 */
public enum Method {
    GET("Get"),
    POST("Post"),
    PUT("Put");

    private String value;

    Method(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
