package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.message.Message;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.TopicRepository;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.DTO.TopicDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageService {
    private static EmployeeRepository employeeRepository;
    private static TopicRepository topicRepository;

    // TODO: TTL (Time To Live / Expiration Date) individually for message? (is this really a requirement?)
    // TODO: JNDI ConnectionFactory instead of ActiveMQConnectionFactory?

    MessageService() {
        employeeRepository = RepositoryFactory.getEmployeeRepositoryInstance();
        topicRepository = RepositoryFactory.getTopicRepositoryInstance();
    }

    private static final String BROKER_URL = "tcp://10.0.40.166:61616";
    private static final long MSG_TTL = TimeUnit.DAYS.toMillis(7); // Time To Live (set to 0 for no expiry)
    private static final long MSG_RECEIVE_TIMEOUT = 5000; // in milliseconds

    private static Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = connectionFactory.createConnection();

        return connection;
    }

    public void publishOrder(MediumDTO mediumDTO, String quantity) throws MessagingException {
        MessageDTO message = DTOProvider.buildMessageDTO(Message.of(
                "Order",
                "Order Inquiry",
                "Order medium ID: " + mediumDTO.id() + "\n" +
                        "Order medium amount: " + quantity
        ));

        publish(message);
    }

    public void publish(MessageDTO message) throws MessagingException {
        try {
            Connection connection = createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            Topic topic = RepositoryFactory.getTopicRepositoryInstance().findAllTopics().stream().filter(topic1 -> {
                try {
                    return topic1.getTopicName().equals(message.topic().name());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }).findFirst().get();

            MessageProducer messageProducer = session.createProducer(topic);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = session.createTextMessage(message.body());
            textMessage.setJMSCorrelationID(message.title());
            messageProducer.send(textMessage);

            messageProducer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred sending a message.");
        }
    }

    public Set<MessageDTO> receiveMessages(String userId) throws MessagingException {
        Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(userId).orElseThrow().getSubscribedTopics();
        Set<MessageDTO> messages = new HashSet<>();

        try {
            Connection connection = createConnection();
            connection.setClientID(userId);
            connection.start();

            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (Topic subscribedTopic : subscribedTopics) {
                MessageConsumer messageConsumer = session.createDurableSubscriber(subscribedTopic, userId+"_"+subscribedTopic.getTopicName());

                // TODO: Think about using an message listener with onMessage event and client callback for non-blocking message receiving
                // TODO: Test if message receive works
                TextMessage textMessage;
                while ((textMessage = (TextMessage) messageConsumer.receive(MSG_RECEIVE_TIMEOUT)) != null) {
                    messages.add(DTOProvider.buildMessageDTO(Message.of(subscribedTopic.getTopicName(), textMessage.getJMSCorrelationID(), textMessage.getText())));
                }

                messageConsumer.close();
            }

            session.close();
            connection.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred receiving a message.");
        }

        return messages;
    }

    public Set<TopicDTO> getAllTopics() {
        return topicRepository.findAllTopics().stream()
                .map(DTOProvider::buildTopicDTO)
                .collect(Collectors.toUnmodifiableSet());
    }
}
