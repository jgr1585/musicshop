package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.communication.Session;
import at.fhv.teamd.musicshop.backend.domain.user.UserRole;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;

import javax.jms.JMSException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {

    private final Session session;

    private ApplicationClientImpl(String userId) throws RemoteException {
        super(ApplicationServer.RMI_BIND_PORT);
        this.session = new Session(userId,
                ServiceFactory.getAuthServiceInstance(),
                ServiceFactory.getMessageServiceInstance().createConnection(userId)
        );
    }

    public static ApplicationClientImpl newInstance(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        AuthService.authenticate(authUser, authPassword);

        return new ApplicationClientImpl(authUser);
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException, NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getCustomerServiceInstance().searchCustomersByName(name);
    }

    @Override
    public boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.ADMIN); // TODO: For presentation/demonstration purposes only (normally only requires SELLER)

        return ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(session.getUserId(), mediumDTO, amount);
    }

    @Override
    public boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(session.getUserId(), mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(session.getUserId());
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(session.getUserId(), customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(session.getUserId());
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getMessageServiceInstance().publishOrder(session, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        session.getAuthService().authorizeAccessLevel(UserRole.OPERATOR);

        ServiceFactory.getMessageServiceInstance().publish(session, message);
    }

    @Override
    public boolean receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getMessageServiceInstance().receiveMessages(session);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        session.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getMessageServiceInstance().getAllTopics();
    }

    @Override
    public void destroy() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);
        try {
            session.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
