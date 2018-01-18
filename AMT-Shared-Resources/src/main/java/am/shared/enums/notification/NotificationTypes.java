package am.shared.enums.notification;

/**
 * Created by ahmed.motair on 1/14/2018.
 */
public enum NotificationTypes {
    EMAIL("EM"),
    SMS("SMS"),
    WEB_NOTIFICATION("WN");

    private final String type;

    NotificationTypes(String type){
        this.type = type;
    }

    public String type(){
        return type;
    }

    public static NotificationTypes getNotificationType(String type) throws Exception{
        for (NotificationTypes notificationType : NotificationTypes.values()) {
            if (notificationType.type.equals(type))
                return notificationType;
        }
        throw new Exception("Invalid Type");
    }
}
