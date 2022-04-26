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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
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

    private Connection createConnection(String userId) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(BROKER_URL).createConnection();
        connection.setClientID(userId);
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);
    }

    private Session getOrInitJmsSession(ApplicationClientSession applicationClientSession) {
        Connection connection = applicationClientSession.getSessionObjectOrCallInitializer("activeMQConnection", () -> createConnection(applicationClientSession.getUserId()), Connection.class);
        Session session = applicationClientSession.getSessionObjectOrCallInitializer("activeMQSession", () -> createSession(connection), Session.class);
        applicationClientSession.getSessionObjectOrCallInitializer("messages", LinkedHashSet::new);

        return session;
    }

    public void publishOrder(ApplicationClientSession applicationClientSession, MediumDTO mediumDTO, String quantity) throws MessagingException {
        Message sendMsg = Message.of(
                "Order",
                "Order Inquiry",
                "Order medium ID: " + mediumDTO.id() + "\n" +
                        "Order medium amount: " + quantity
        );

        publish(applicationClientSession, sendMsg);
    }

    public void publish(ApplicationClientSession applicationClientSession, MessageDTO message) throws MessagingException {
        publish(applicationClientSession, Message.of(message.topic().name(), message.title(), message.body()));
    }

    private void publish(ApplicationClientSession applicationClientSession, Message message) throws MessagingException {
        try {
            Session session = getOrInitJmsSession(applicationClientSession);

            MessageProducer messageProducer = session.createProducer(session.createTopic(message.getTopicName()));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = session.createTextMessage(message.getBody());
            textMessage.setJMSMessageID(String.valueOf(UUID.randomUUID()));
            textMessage.setStringProperty("title", message.getTitle());
            messageProducer.send(textMessage);

            messageProducer.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred sending a message.");
        }
    }

    // TODO: fix?
    public Set<MessageDTO> receive(ApplicationClientSession applicationClientSession) {
        Set<javax.jms.Message> messages = (Set<javax.jms.Message>) applicationClientSession.getSessionObject("messages", Set.class);

        return messages.stream()
                .sorted()
                .map(message -> {
                    System.out.println(message);
                    try {
                        return DTOProvider.buildMessageDTO(
                                Message.of(
                                        message.getJMSDestination().toString(),
                                        message.getStringProperty("title"),
                                        ((TextMessage) message).getText(),
                                        Instant.ofEpochSecond(message.getJMSTimestamp()))
                        );
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void acknowledge(ApplicationClientSession applicationClientSession, MessageDTO message) {
        // TODO: exceptions
        try {
            Set<javax.jms.Message> messages = (Set<javax.jms.Message>) applicationClientSession.getSessionObject("messages", Set.class);

            messages.stream()
                    .filter(message1 -> {
                        try {
                            return message1.getJMSMessageID().equals(message.uuid());
                        } catch (JMSException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .findFirst().orElseThrow().acknowledge();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: private static?
    public void receiveMessages(ApplicationClientSession applicationClientSession) throws MessagingException {
        Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(applicationClientSession.getUserId()).orElseThrow().getSubscribedTopics();

        try {
            Session session = getOrInitJmsSession(applicationClientSession);

            for (Topic subscribedTopic : subscribedTopics) {
                MessageConsumer messageConsumer = session.createDurableSubscriber(subscribedTopic, applicationClientSession.getUserId() + "_" + subscribedTopic.getTopicName());
                messageConsumer.setMessageListener(new ConsumerMessageListener(applicationClientSession.getUserId() + "_" + subscribedTopic.getTopicName(), applicationClientSession));
            }
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new MessagingException("An error occurred receiving a message.");
        }
    }

    public Set<TopicDTO> getAllTopics() {
        return topicRepository.findAllTopics().stream()
                .sorted()
                .map(DTOProvider::buildTopicDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static class ConsumerMessageListener implements MessageListener {
        private final String consumerName;
        private final ApplicationClientSession applicationClientSession;

        public ConsumerMessageListener(String consumerName, ApplicationClientSession applicationClientSession) {
            this.consumerName = consumerName;
            this.applicationClientSession = applicationClientSession;
        }

        @Override
        public void onMessage(javax.jms.Message message) {
            try {
                Set<javax.jms.Message> messages = (Set<javax.jms.Message>) applicationClientSession.getSessionObject("messages", Set.class);

                System.out.println(consumerName + " received " + ((TextMessage) message).getText() + "; " + message.getJMSMessageID());
                messages.add(message);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
