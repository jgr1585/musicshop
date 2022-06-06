package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.dto.*;
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
        authService.authorizeAccessLevels(RemoteFunctionPermission.SEARCH_ARTICLES_BY_ATTRIBUTES);

        return articleService.searchPhysicalMediumArticles(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.SEARCH_CUSTOMERS_BY_NAME);


        try {
            return customerService.searchCustomersByName(name);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public InvoiceDTO findInvoiceById(Long id) throws NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.FIND_INVOICE_BY_ID);

        return invoiceService.searchInvoiceById(id);
    }

    @Override
    public void returnItem(LineItemDTO lineItem, int quantity) throws NotAuthorizedException, InvoiceException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.RETURN_ITEM);

        invoiceService.returnItem(lineItem, quantity);
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.ADD_TO_SHOPPING_CART);

        shoppingCartService.addPhysicalMediumToShoppingCart(userId, mediumDTO.id(), amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.REMOVE_FROM_SHOPPING_CART);

        shoppingCartService.removePhysicalMediumFromShoppingCart(userId, mediumDTO.id(), amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.EMPTY_SHOPPING_CART);

        shoppingCartService.emptyPhysicalMediumShoppingCart(userId);
    }

    @Override
    public String buyFromShoppingCart(int customerId) throws NotAuthorizedException, ShoppingCartException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.BUY_FROM_SHOPPING_CART);

        return shoppingCartService.buyFromPhysicalMediumShoppingCart(userId, customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.GET_SHOPPING_CART);

        return shoppingCartService.getPhysicalMediumShoppingCart(userId);
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.PUBLISH_ORDER_MESSAGE);

        messageService.publishOrder(userId, mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.PUBLISH_MESSAGE);

        messageService.publish(userId, message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.RECEIVE_MESSAGES);

        return messageService.receive(userId);
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.ACKNOWLEDGE_MESSAGE);

        messageService.acknowledge(userId, message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws NotAuthorizedException {
        authService.authorizeAccessLevels(RemoteFunctionPermission.GET_ALL_TOPICS);

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
