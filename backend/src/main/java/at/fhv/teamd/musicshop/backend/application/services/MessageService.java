package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.application.SessionObject;
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

    public void publishOrder(SessionObject sessionObject, MediumDTO mediumDTO, String quantity) throws MessagingException {
        Message sendMsg = Message.of(
                "Order",
                "Order Inquiry",
                "Order medium ID: " + mediumDTO.id() + "\n" +
                        "Order medium amount: " + quantity
        );

        publish(sessionObject, sendMsg);
    }

    public void publish(SessionObject sessionObject, MessageDTO message) throws MessagingException {
        publish(sessionObject, Message.of(message.topic().name(), message.title(), message.body()));
    }

    private void publish(SessionObject sessionObject, Message message) throws MessagingException {
        try {

            MessageProducer messageProducer = sessionObject.getActiveMQSession().createProducer(sessionObject.getActiveMQSession().createTopic(message.getTopicName()));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = sessionObject.getActiveMQSession().createTextMessage(message.getBody());
            textMessage.setJMSMessageID(String.valueOf(UUID.randomUUID()));
            textMessage.setStringProperty("title", message.getTitle());
            messageProducer.send(textMessage);

            messageProducer.close();

        } catch (JMSException e) {
            throw new MessagingException("An error occurred sending a message.");
        }
    }

    // TODO: fix?
    public Set<MessageDTO> receive(SessionObject sessionObject) {
        return sessionObject.getMessages().stream()
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

    public void acknowledge(SessionObject sessionObject, MessageDTO message) {
        // TODO: exceptions
        try {
            sessionObject.getMessages().stream()
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
    public void receiveMessages(SessionObject sessionObject) throws MessagingException {
        Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(sessionObject.getUserId()).orElseThrow().getSubscribedTopics();

        try {
            for (Topic subscribedTopic : subscribedTopics) {
                MessageConsumer messageConsumer = sessionObject.getActiveMQSession().createDurableSubscriber(subscribedTopic, sessionObject.getUserId() + "_" + subscribedTopic.getTopicName());
                messageConsumer.setMessageListener(new ConsumerMessageListener(sessionObject.getUserId() + "_" + subscribedTopic.getTopicName(), sessionObject));
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
        private final SessionObject sessionObject;

        public ConsumerMessageListener(String consumerName, SessionObject sessionObject) {
            this.consumerName = consumerName;
            this.sessionObject = sessionObject;
        }

        @Override
        public void onMessage(javax.jms.Message message) {
            try {
                System.out.println(consumerName + " received " + ((TextMessage) message).getText() + "; " + message.getJMSMessageID());
                sessionObject.getMessages().add(message);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
