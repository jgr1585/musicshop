package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Set;

// TODO: still to-do: remove services constructor dependency injection of applicationClientSession and use field-based approach (may use a SessionService to register ApplicationClientSessions -> but think this well-through as this might be hacky)
@Stateful
public class ApplicationClientImpl implements ApplicationClient {

    private final AuthService authService = ServiceFactory.getAuthServiceInstance();
    private final ArticleService articleService = ServiceFactory.getArticleServiceInstance();
    private final CustomerService customerService = ServiceFactory.getCustomerServiceInstance();
    private final InvoiceService invoiceService = ServiceFactory.getInvoiceServiceInstance();
    private final MessageService messageService = ServiceFactory.getMessageServiceInstance();
    private final ShoppingCartService shoppingCartService = ServiceFactory.getShoppingCartServiceInstance();

    private ApplicationClientSession applicationClientSession;
    private String userId;

    public ApplicationClientImpl() {
        //Required for hibernate
    }

    @Override
    public void authenticate(String authUser, String authPassword) throws AuthenticationFailedException {
        AuthService.authenticateEmployee(authUser, authPassword);
        this.userId = authUser;
    }

    @Override
    public String getSessionUserId() {
        return this.userId;
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.searchArticlesByAttributes);

        return articleService.searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.searchCustomersByName);


        try {
            return customerService.searchCustomersByName(name);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public InvoiceDTO findInvoiceById(Long id) throws NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.findInvoiceById);

        return invoiceService.searchInvoiceById(id);
    }

    @Override
    public void returnItem(LineItemDTO lineItem, int quantity) throws NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.returnItem);

        invoiceService.returnItem(lineItem, quantity);
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.addToShoppingCart);

        shoppingCartService.addToShoppingCart(userId, mediumDTO.id(), amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.removeFromShoppingCart);

        shoppingCartService.removeFromShoppingCart(userId, mediumDTO.id(), amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.emptyShoppingCart);

        shoppingCartService.emptyShoppingCart(userId);
    }

    @Override
    public String buyFromShoppingCart(int customerId) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.buyFromShoppingCart);

        return shoppingCartService.buyFromShoppingCart(userId, customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.getShoppingCart);

        return shoppingCartService.getShoppingCart(userId);
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.publishOrderMessage);

        messageService.publishOrder(userId, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.publishMessage);

        messageService.publish(userId, message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.receiveMessages);

        return messageService.receive(userId);
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.acknowledgeMessage);

        messageService.acknowledge(userId, message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.getAllTopics);

        return messageService.getAllTopics();
    }

    @Override
    public boolean isAuthorizedFor(RemoteFunctionPermission functionPermission) {
        try {
            authService.authorizeAccessLevels(functionPermission);
            return true;
        } catch (NotAuthorizedException e) {
            return false;
        }
    }
}
