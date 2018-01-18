package am.shared.enums;

import static am.shared.common.JMSParams.*;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
public enum JMSQueues {
    LOG4J2(LOG4J2_Q, "Log4j2"),
    NOTIFICATION_INPUT(NOTIFICATION_INPUT_Q, "Notification Input"),
    NOTIFICATION_PROCESS(NOTIFICATION_PROCESS_Q, "Notification Process"),
    NOTIFICATION_VALIDATION(NOTIFICATION_VALIDATION_Q, "Notification Validation"),
    EMAIL_NOTIFICATION(EMAIL_NOTIFICATION_Q, "Email Notification Sender"),
    SMS_NOTIFICATION(SMS_NOTIFICATION_Q, "SMS Notification Sender"),
    WEB_NOTIFICATION(WEB_NOTIFICATION_Q, "Web Notification Sender");

    private final String queueName;
    private final String description;

    JMSQueues(String queueName, String description){
        this.queueName = queueName;
        this.description = description;
    }

    public String getQueueName() {
        return QUEUE + queueName;
    }
    public String getConnectionFactory() {
        return QUEUE_CONNECTION_FACTORY + queueName;
    }
    public String description() {
        return this.description;
    }
}
