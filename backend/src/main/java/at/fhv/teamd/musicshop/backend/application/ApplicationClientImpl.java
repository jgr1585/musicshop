package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

// TODO: still to-do: remove services constructor dependency injection of applicationClientSession and use field-based approach (may use a SessionService to register ApplicationClientSessions -> but think this well-through as this might be hacky)
@Remote(ApplicationClient.class)
@Stateful
public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {

    private final AuthService authService = ServiceFactory.getAuthServiceInstance();
    private final ArticleService articleService = ServiceFactory.getArticleServiceInstance();
    private final CustomerService customerService = ServiceFactory.getCustomerServiceInstance();
    private final InvoiceService invoiceService = ServiceFactory.getInvoiceServiceInstance();
    private final MessageService messageService = ServiceFactory.getMessageServiceInstance();
    private final ShoppingCartService shoppingCartService = ServiceFactory.getShoppingCartServiceInstance();

    private final ApplicationClientSession applicationClientSession;

    public ApplicationClientImpl() throws RemoteException {
        this("lka3333");
    }

    private ApplicationClientImpl(String userId) throws RemoteException {
        super(ApplicationServer.RMI_BIND_PORT);

        this.applicationClientSession = new ApplicationClientSession(userId);
    }

    public static ApplicationClientImpl newInstance(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        AuthService.authenticate(authUser, authPassword);

        return new ApplicationClientImpl(AuthService.getUserName());
    }

    @Override
    public String getSessionUserId() {
        return applicationClientSession.getUserId();
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.searchArticlesByAttributes);

        return articleService.searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.searchCustomersByName);

        return customerService.searchCustomersByName(name);
    }

    @Override
    public InvoiceDTO findInvoiceById(Long id) throws RemoteException, NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.findInvoiceById);

        return invoiceService.searchInvoiceById(id);
    }

    @Override
    public void returnItem(LineItemDTO lineItem, int quantity) throws RemoteException, NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.returnItem);

        invoiceService.returnItem(lineItem, quantity);
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.addToShoppingCart);

        shoppingCartService.addToShoppingCart(applicationClientSession.getUserId(), mediumDTO, amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.removeFromShoppingCart);

        shoppingCartService.removeFromShoppingCart(applicationClientSession.getUserId(), mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.emptyShoppingCart);

        shoppingCartService.emptyShoppingCart(applicationClientSession.getUserId());
    }

    @Override
    public void buyFromShoppingCart(int customerId) throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.buyFromShoppingCart);

        shoppingCartService.buyFromShoppingCart(applicationClientSession.getUserId(), customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.getShoppingCart);

        return shoppingCartService.getShoppingCart(applicationClientSession.getUserId());
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.publishOrderMessage);

        messageService.publishOrder(applicationClientSession, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.publishMessage);

        messageService.publish(applicationClientSession, message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.receiveMessages);

        return messageService.receive(applicationClientSession);
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.acknowledgeMessage);

        messageService.acknowledge(applicationClientSession, message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.getAllTopics);

        return messageService.getAllTopics();
    }

    @Override
    public boolean isAuthorizedFor(RemoteFunctionPermission functionPermission) throws RemoteException {
        try {
            authService.authorizeAccessLevels(functionPermission);
            return true;
        } catch (NotAuthorizedException e) {
            return false;
        }
    }

    @Override
    public void destroy() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);

        applicationClientSession.purge();
    }
}
