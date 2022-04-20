package at.fhv.teamd.musicshop.backend.application.services;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import java.util.concurrent.TimeUnit;

public class MessageService {

    // TODO: integrate messageDTO
    // TODO: TTL in messageDTO
    // TODO: JNDI ConnectionFactory
    // TODO: Topic

    private static final String DEFAULT_BROKER_BIND_URL = "tcp://10.0.40.166:61616";

    public boolean publish(String topic, String title, String message) {
        try {

//            // Get the JNDI Initial Context to do JNDI lookups
//            InitialContext ctx = getInitialContext();
//            // Get the ConnectionFactory by JNDI name
//            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/connectionFactory");
//            // get the Destination used to send the message by JNDI name
//            Destination dest = (Destination) ctx.lookup("jms/" + topic);
//            // Create a connection
//            Connection con = cf.createConnection();
//            // create a JMS session
//            Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            // Create some Message and a MessageProducer with the session
//            Message msg = sess.createTextMessage(message);
//            MessageProducer prod = sess.createProducer(dest);
//            // send the message to the destination
//            prod.send(msg);
//            // Close the connection
//            con.close();

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            MessageProducer messageProducer = session.createProducer(session.createTopic(topic));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setJMSCorrelationID(title);
            textMessage.setJMSExpiration(TimeUnit.DAYS.toMillis(3));
            messageProducer.send(textMessage);

            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
