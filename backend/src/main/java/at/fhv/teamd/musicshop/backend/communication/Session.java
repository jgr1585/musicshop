package at.fhv.teamd.musicshop.backend.communication;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import lombok.Getter;

import javax.jms.Connection;
import javax.jms.JMSException;

@Getter
public class Session {

    private final String userId;
    private final AuthService authService;
    private final Connection activeMQConnection;
    private final javax.jms.Session activeMQSession;

    public Session(String userId, AuthService authService, Connection activeMQConnection) {
        this.userId = userId;
        this.authService = authService;
        this.activeMQConnection = activeMQConnection;
        try {
            this.activeMQSession = activeMQConnection.createSession(false, javax.jms.Session.CLIENT_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        run();
    }

    private void run() {
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
