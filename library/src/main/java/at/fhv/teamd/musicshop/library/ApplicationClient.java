package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.dto.*;
import at.fhv.teamd.musicshop.library.exceptions.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;

import javax.ejb.Remote;
import java.util.Set;

@Remote
public interface ApplicationClient {
    void authenticate(String authUser, String authPassword) throws AuthenticationFailedException;

    String getSessionUserId();

    // Search Articles
    Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException;

    // Search Customers
    Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, NotAuthorizedException;

    // Invoice
    InvoiceDTO findInvoiceById(Long id) throws NotAuthorizedException, InvoiceException;

    // Return
    void returnItem(LineItemDTO lineItem, int quantity) throws NotAuthorizedException, InvoiceException;

    // Shopping Cart
    void addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException;

    void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException;

    void emptyShoppingCart() throws NotAuthorizedException;

    String buyFromShoppingCart(int customerId) throws NotAuthorizedException, ShoppingCartException;

    ShoppingCartDTO getShoppingCart() throws NotAuthorizedException, ShoppingCartException;

    // Messaging
    void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws NotAuthorizedException, MessagingException;

    void publishMessage(MessageDTO message) throws NotAuthorizedException, MessagingException;

    Set<MessageDTO> receiveMessages() throws NotAuthorizedException, MessagingException;

    void acknowledgeMessage(MessageDTO message) throws NotAuthorizedException, MessagingException;

    Set<TopicDTO> getAllTopics() throws NotAuthorizedException;

    // Authorization
    boolean isAuthorizedFor(RemoteFunctionPermission functionPermission);
}
