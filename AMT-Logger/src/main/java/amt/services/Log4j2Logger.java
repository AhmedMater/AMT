package amt.services;

import am.main.api.AppLogger;
import am.main.api.MessageHandler;
import am.main.api.db.DBManager;
import am.main.data.dto.AMLogData;
import am.main.session.AppSession;
import am.shared.enums.Interface;
import am.shared.enums.Phase;
import am.shared.enums.Source;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import static am.shared.common.JMSParams.LOG4J2;
import static am.shared.common.JMSParams.QUEUE;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
@MessageDriven(mappedName = QUEUE + LOG4J2)
public class Log4j2Logger implements MessageListener {
    @Resource private MessageDrivenContext context;
    @Inject private DBManager dbManager;

    private final String CLASS = Log4j2Logger.class.getSimpleName();

    @Inject private MessageHandler messageHandler;
    @Inject private AppLogger logger;

    @Override
    public void onMessage(Message m) {
        String METHOD = "onMessage";
        try {
            String jmsID = m.getJMSMessageID();
            AppSession session = new AppSession(Source.AMT_LOGGER, Interface.JMS, Phase.AMT_LOGGING);

            if (m instanceof ObjectMessage) {
                ObjectMessage message = (ObjectMessage) m;
                AMLogData logData = message.getBody(AMLogData.class);

                logData.getSession().setMessageHandler(messageHandler);
                logger.log(session, logData);
            }
        }catch (Exception ex){
            logger.FAILURE_LOGGER.error(ex);
        }
    }
}