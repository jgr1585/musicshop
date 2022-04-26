package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.*;
import at.fhv.teamd.musicshop.backend.domain.user.UserRole;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

// TODO: still to-do: remove services constructor dependency injection of applicationClientSession and use field-based approach (may use a SessionService to register ApplicationClientSessions -> but think this well-through as this might be hacky)
public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {

    private final AuthService authService = ServiceFactory.getAuthServiceInstance();
    private final ArticleService articleService = ServiceFactory.getArticleServiceInstance();
    private final CustomerService customerService = ServiceFactory.getCustomerServiceInstance();
    private final MessageService messageService = ServiceFactory.getMessageServiceInstance();
    private final ShoppingCartService shoppingCartService = ServiceFactory.getShoppingCartServiceInstance();

    private final ApplicationClientSession applicationClientSession;

    private ApplicationClientImpl(String userId) throws RemoteException {
        super(ApplicationServer.RMI_BIND_PORT);

        this.applicationClientSession = new ApplicationClientSession(userId);
    }

    public static ApplicationClientImpl newInstance(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        AuthService.authenticate(authUser, authPassword);

        return new ApplicationClientImpl(authUser);
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return articleService.searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return customerService.searchCustomersByName(name);
    }

    @Override
    public boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.ADMIN); // TODO: For presentation/demonstration purposes only (normally only requires SELLER)

        return shoppingCartService.addToShoppingCart(applicationClientSession.getUserId(), mediumDTO, amount);
    }

    @Override
    public boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return shoppingCartService.removeFromShoppingCart(applicationClientSession.getUserId(), mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        shoppingCartService.emptyShoppingCart(applicationClientSession.getUserId());
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return shoppingCartService.buyFromShoppingCart(applicationClientSession.getUserId(), customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return shoppingCartService.getShoppingCart(applicationClientSession.getUserId());
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        messageService.publishOrder(applicationClientSession, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevel(UserRole.OPERATOR);

        messageService.publish(applicationClientSession, message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return messageService.receive(applicationClientSession);
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        messageService.acknowledge(applicationClientSession, message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return messageService.getAllTopics();
    }

    @Override
    public void destroy() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);

        applicationClientSession.purge();
    }
}
