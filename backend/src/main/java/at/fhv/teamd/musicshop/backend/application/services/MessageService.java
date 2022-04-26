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
import java.util.LinkedHashSet;
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

    public Connection createConnection(String userId) {
        try {
            Connection connection = new ActiveMQConnectionFactory(BROKER_URL).createConnection();
            connection.setClientID(userId);
            connection.start();
            return connection;
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrder(at.fhv.teamd.musicshop.backend.communication.Session clientsession, MediumDTO mediumDTO, String quantity) throws MessagingException {
        MessageDTO message = DTOProvider.buildMessageDTO(Message.of(
                "Order",
                "Order Inquiry",
                "Order medium ID: " + mediumDTO.id() + "\n" +
                        "Order medium amount: " + quantity
        ));

        publish(clientsession, message);
    }

    public void publish(at.fhv.teamd.musicshop.backend.communication.Session session, MessageDTO message) throws MessagingException {
        try {

            MessageProducer messageProducer = session.getActiveMQSession().createProducer(session.getActiveMQSession().createTopic(message.topic().name()));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = session.getActiveMQSession().createTextMessage(message.body());
            textMessage.setJMSCorrelationID(message.title());
            messageProducer.send(textMessage);

            messageProducer.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred sending a message.");
        }
    }

    public Set<MessageDTO> receiveMessages(at.fhv.teamd.musicshop.backend.communication.Session session) throws MessagingException {
        Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(session.getUserId()).orElseThrow().getSubscribedTopics();
        Set<MessageDTO> messages = new HashSet<>();

        try {
            for (Topic subscribedTopic : subscribedTopics) {
                MessageConsumer messageConsumer = session.getActiveMQSession().createDurableSubscriber(subscribedTopic, session.getUserId() + "_" + subscribedTopic.getTopicName());
                messageConsumer.setMessageListener(new ConsumerMessageListener(session.getUserId() + "_" + subscribedTopic.getTopicName()));
            }
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new MessagingException("An error occurred receiving a message.");
        }

        return messages;
    }

    public Set<TopicDTO> getAllTopics() {
        return topicRepository.findAllTopics().stream()
                .sorted()
                .map(DTOProvider::buildTopicDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
