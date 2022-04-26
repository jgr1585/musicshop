package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
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

    private final SessionObject sessionObject;

    private ApplicationClientImpl(String userId) throws RemoteException {
        super(ApplicationServer.RMI_BIND_PORT);
        this.sessionObject = new SessionObject(userId,
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
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException, NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getCustomerServiceInstance().searchCustomersByName(name);
    }

    @Override
    public boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.ADMIN); // TODO: For presentation/demonstration purposes only (normally only requires SELLER)

        return ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(sessionObject.getUserId(), mediumDTO, amount);
    }

    @Override
    public boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(sessionObject.getUserId(), mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(sessionObject.getUserId());
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(sessionObject.getUserId(), customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(sessionObject.getUserId());
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getMessageServiceInstance().publishOrder(sessionObject, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.OPERATOR);

        ServiceFactory.getMessageServiceInstance().publish(sessionObject, message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getMessageServiceInstance().receiveMessages(sessionObject);
        return this.sessionObject.getMessages();
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        sessionObject.getAuthService().authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getMessageServiceInstance().getAllTopics();
    }

    @Override
    public void destroy() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);
        try {
            sessionObject.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
