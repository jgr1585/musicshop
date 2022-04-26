package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.MessageDTO;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import lombok.Getter;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class SessionObject {

    private final String userId;
    private final AuthService authService;
    private final Connection activeMQConnection;
    private final javax.jms.Session activeMQSession;

    private final Set<Message> messages = new LinkedHashSet<>();

    public SessionObject(String userId, AuthService authService, Connection activeMQConnection) {
        this.userId = userId;
        this.authService = authService;
        this.activeMQConnection = activeMQConnection;
        try {
            this.activeMQSession = activeMQConnection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        try {
            ServiceFactory.getMessageServiceInstance().receiveMessages(this);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws JMSException {
        activeMQSession.close();
        activeMQConnection.close();
    }
}
