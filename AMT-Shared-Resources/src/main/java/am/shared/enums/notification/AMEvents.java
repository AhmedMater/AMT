package am.shared.enums.notification;

/**
 * Created by ahmed.motair on 1/13/2018.
 */
public enum AMEvents {
    NEW_COURSE("NCor");

    private String eventName;

    AMEvents(String eventName){
        this.eventName = eventName;
    }

    public String eventName() {
        return eventName;
    }
}
