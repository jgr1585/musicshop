package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.application.ApplicationClientSession;
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
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageService {
    private static EmployeeRepository employeeRepository;
    private static TopicRepository topicRepository;

    // TODO: JNDI ConnectionFactory instead of ActiveMQConnectionFactory?

    MessageService() {
        employeeRepository = RepositoryFactory.getEmployeeRepositoryInstance();
        topicRepository = RepositoryFactory.getTopicRepositoryInstance();
    }

    private static final String BROKER_URL = "tcp://10.0.40.166:61616";
    private static final long MSG_TTL = TimeUnit.DAYS.toMillis(7); // Time To Live (set to 0 for no expiry)
    private static final long TIMEOUT = 5000;

    private Connection createConnection(String userId) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(BROKER_URL).createConnection();
        connection.setClientID(userId);
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);
    }

    private Session initJMS(ApplicationClientSession applicationClientSession) throws MessagingException {
        Connection connection = applicationClientSession.getSessionObjectOrCallInitializer("activeMQConnection", () -> createConnection(applicationClientSession.getUserId()), Connection.class);
        Session session = applicationClientSession.getSessionObjectOrCallInitializer("activeMQSession", () -> createSession(connection), Session.class);

        if (!applicationClientSession.containsSessionObject("messageConsumers")) {
            Set<MessageConsumer> messageConsumers = (Set<MessageConsumer>) applicationClientSession.getSessionObjectOrCallInitializer("messageConsumers", HashSet::new, Set.class);
            Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(applicationClientSession.getUserId()).orElseThrow().getSubscribedTopics();
            for (Topic topic : subscribedTopics) {
                try {
                    messageConsumers.add(session.createDurableSubscriber(topic, applicationClientSession.getUserId() + "_" + topic.getTopicName()));
                } catch (JMSException e) {
                    throw new MessagingException("An error occurred: " + e.getMessage());
                }
            }
        }

        return session;
    }

    public void publishOrder(ApplicationClientSession applicationClientSession, MediumDTO mediumDTO, String quantity) throws MessagingException {
        Message sendMsg = Message.of("Order", "Order Inquiry", "Order medium ID: " + mediumDTO.id() + "\n" + "Order medium amount: " + quantity);

        publish(applicationClientSession, sendMsg);
    }

    public void publish(ApplicationClientSession applicationClientSession, MessageDTO message) throws MessagingException {
        publish(applicationClientSession, Message.of(message.topic().name(), message.title(), message.body()));
    }

    private void publish(ApplicationClientSession applicationClientSession, Message message) throws MessagingException {
        try {
            Session session = initJMS(applicationClientSession);

            MessageProducer messageProducer = session.createProducer(session.createTopic(message.getTopicName()));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = session.createTextMessage(message.getBody());
            textMessage.setStringProperty("id", message.getId());
            textMessage.setStringProperty("title", message.getTitle());
            messageProducer.send(textMessage);

            messageProducer.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred: " + e.getMessage());
        }
    }

    public Set<MessageDTO> receive(ApplicationClientSession applicationClientSession) throws MessagingException {

        initJMS(applicationClientSession);

        Set<javax.jms.Message> messages = new LinkedHashSet<>();

        for (MessageConsumer messageConsumer : (Set<MessageConsumer>) applicationClientSession.getSessionObject("messageConsumers", Set.class)) {
            try {
                javax.jms.Message message;
                while ((message = messageConsumer.receive(TIMEOUT)) != null) {
                    messages.add(message);
                    System.out.println("received " + ((TextMessage) message).getText() + "; " + message.getJMSMessageID());
                }
            } catch (JMSException e) {
                throw new MessagingException("An error occurred: " + e.getMessage());
            }
        }

        applicationClientSession.setSessionObject("messages", messages);

        Set<MessageDTO> messageDTOs = new LinkedHashSet<>();

        messages.forEach(message -> {
            try {
                messageDTOs.add(
                        DTOProvider.buildMessageDTO(
                                Message.of(
                                        message.getStringProperty("id"),
                                        message.getJMSDestination().toString(),
                                        message.getStringProperty("title"),
                                        ((TextMessage) message).getText(),
                                        Instant.ofEpochMilli(message.getJMSTimestamp()))
                        )
                );
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });
        return messageDTOs;
    }

    public void acknowledge(ApplicationClientSession applicationClientSession, MessageDTO messageToAcknowledge) throws MessagingException {
        Set<javax.jms.Message> messages = (Set<javax.jms.Message>) applicationClientSession.getSessionObject("messages", Set.class);

        for (javax.jms.Message message : messages) {
            try {
                if (message.getStringProperty("id").equals(messageToAcknowledge.uuid())) {
                    message.acknowledge();
                }
            } catch (JMSException e) {
                throw new MessagingException("An error occurred: " + e.getMessage());
            }
        }
    }

    public Set<TopicDTO> getAllTopics() {
        return topicRepository.findAllTopics()
                .stream()
                .sorted()
                .map(DTOProvider::buildTopicDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
