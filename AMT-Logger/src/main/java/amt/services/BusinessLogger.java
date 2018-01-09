package amt.services;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

import static am.shared.common.JMSParams.BUSINESS;
import static am.shared.common.JMSParams.QUEUE;

/**
 * Created by ahmed.motair on 1/5/2018.
 */
@MessageDriven(mappedName = QUEUE + BUSINESS)
public class BusinessLogger implements MessageListener {
    @Resource
    private MessageDrivenContext context;

    @Override
    public void onMessage(Message m) {
        System.out.println(m);
        //nprService.processInboundMsg(context, m);
    }
}