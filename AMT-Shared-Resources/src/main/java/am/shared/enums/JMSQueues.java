package am.shared.enums;

import static am.shared.common.JMSParams.*;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
public enum JMSQueues {
    LOG4J2_Q(QUEUE + LOG4J2, QUEUE_CONNECTION_FACTORY + LOG4J2),
    BUSINESS_Q(QUEUE + BUSINESS, QUEUE_CONNECTION_FACTORY + BUSINESS),
    PERFORMANCE_Q(QUEUE + PERFORMANCE, QUEUE_CONNECTION_FACTORY + PERFORMANCE);

    private final String queueName;
    private final String connectionFactory;

    JMSQueues(String queueName, String connectionFactory){
        this.queueName = queueName;
        this.connectionFactory = connectionFactory;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getConnectionFactory() {
        return connectionFactory;
    }
}
