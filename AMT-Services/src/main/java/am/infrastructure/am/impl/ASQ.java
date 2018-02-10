package am.infrastructure.am.impl;

import am.main.spi.JMSQueues;

/**
 * Created by ahmed.motair on 2/10/2018.
 */
public class ASQ extends JMSQueues {
    private static String PREFIX = "AMT";

    public static final ASQ NOTIFICATION = new ASQ("Notification", "Queue for Sending Notification to AM-Notification");

    private ASQ(String queueName, String description) {
        super(PREFIX, queueName, description);
    }
}
