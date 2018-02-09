package am.infrastructure.am.notification;

import am.main.spi.notification.AMEvent;

/**
 * Created by ahmed.motair on 1/31/2018.
 */
public class AMTEvent extends AMEvent {

    public static final AMTEvent NEW_COURSE = new AMTEvent("NC");
    public static final AMTEvent NEW_USER = new AMTEvent("NU");

    private AMTEvent(String event) {
        super(event);
    }
}
