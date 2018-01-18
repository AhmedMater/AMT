package am.shared.enums.notification;

/**
 * Created by ahmed.motair on 1/14/2018.
 */
public enum AMEventNotifications {
    IS("IS"),
    ITR("ITR"),
    ITT("ITT");

    private final String eventNotification;

    AMEventNotifications(String eventNotification){
        this.eventNotification = eventNotification;
    }

    public String eventNotification() {
        return eventNotification;
    }

}
