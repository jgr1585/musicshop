package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.message.Message;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.TopicRepository;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;
import at.fhv.teamd.musicshop.library.dto.MessageDTO;
import at.fhv.teamd.musicshop.library.dto.TopicDTO;
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
    private static final EmployeeRepository employeeRepository = RepositoryFactory.getEmployeeRepositoryInstance();
    private static final TopicRepository topicRepository = RepositoryFactory.getTopicRepositoryInstance();

    private static final String BROKER_URL = "tcp://10.0.40.166:61616";
    private static final long MSG_TTL = TimeUnit.DAYS.toMillis(7);
    private static final long TIMEOUT = 30;

    private Connection createConnection(String userId) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory(BROKER_URL).createConnection();
        connection.setClientID(userId);
        connection.start();
        return connection;
    }

    public void publishOrder(String userId, MediumDTO mediumDTO, String quantity) throws MessagingException {
        if (Integer.parseInt(quantity) < 1) throw new IllegalArgumentException("Quantity to order to small");
        Message sendMsg = Message.of(
                "Order",
                "Order medium ID: " + mediumDTO.id() + "; " + "Order medium amount: " + quantity,
                "Order medium ID: " + mediumDTO.id() + "\n" + "Order medium amount: " + quantity
        );

        publish(userId, sendMsg);
    }

    public void publish(String userId, MessageDTO message) throws MessagingException {
        publish(userId, Message.of(message.topic().name(), message.title(), message.body()));
    }

    private void publish(String userId, Message message) throws MessagingException {
        try {
            Connection connection = createConnection(userId);
            Session session = connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);

            MessageProducer messageProducer = session.createProducer(session.createTopic(message.getTopicName()));
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
            messageProducer.setTimeToLive(MSG_TTL);

            TextMessage textMessage = session.createTextMessage(message.getBody());
            textMessage.setStringProperty("id", message.getId());
            textMessage.setStringProperty("title", message.getTitle());
            messageProducer.send(textMessage);

            messageProducer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            throw new MessagingException("An error occurred while publishing a message: " + e.getMessage());
        }
    }

    public Set<MessageDTO> receive(String userId) throws MessagingException {
        try {
            Connection connection = createConnection(userId);
            Session session = connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);

            Set<MessageConsumer> messageConsumers = new HashSet<>();

            Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(userId).orElseThrow().getSubscribedTopics();

            for (Topic topic : subscribedTopics) {
                messageConsumers.add(session.createDurableSubscriber(topic, userId + "_" + topic.getTopicName()));
            }

            Set<javax.jms.Message> messages = new LinkedHashSet<>();

            for (MessageConsumer messageConsumer : messageConsumers) {
                javax.jms.Message message;
                while ((message = messageConsumer.receive(TIMEOUT)) != null) {
                    messages.add(message);
                }
            }

            Set<MessageDTO> messageDTOs = new LinkedHashSet<>();

            for (javax.jms.Message message : messages) {
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
            }

            session.close();
            connection.close();
            return messageDTOs;

        } catch (JMSException e) {
            throw new MessagingException("An error occurred while receiving a message: " + e.getMessage());
        }
    }

    public void acknowledge(String userId, MessageDTO messageToAcknowledge) throws MessagingException {
        try {
            Connection connection = createConnection(userId);
            Session session = connection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);

            Set<MessageConsumer> messageConsumers = new HashSet<>();

            Set<Topic> subscribedTopics = employeeRepository.findEmployeeByUserName(userId).orElseThrow().getSubscribedTopics();

            for (Topic topic : subscribedTopics) {
                messageConsumers.add(session.createDurableSubscriber(topic, userId + "_" + topic.getTopicName()));
            }

            for (MessageConsumer messageConsumer : messageConsumers) {
                javax.jms.Message message;
                while ((message = messageConsumer.receive(TIMEOUT)) != null) {
                    if (message.getStringProperty("id").equals(messageToAcknowledge.uuid())) {
                        message.acknowledge();
                        break;
                    }
                }
            }

            session.close();
            connection.close();
        } catch (JMSException e) {
            throw new MessagingException("An error occurred during acknowledging a message: " + e.getMessage());
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