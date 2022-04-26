package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.communication.Session;
import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.DTO.TopicDTO;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {
    private final String consumerName;
    private final Session session;

    public ConsumerMessageListener(String consumerName, Session session) {
        this.consumerName = consumerName;
        this.session = session;
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(consumerName + " received " + ((TextMessage) message).getText() + "; ID: " + message.getJMSMessageID());
            session.getMessages().add(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
