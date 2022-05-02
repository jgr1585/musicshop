package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ApplicationClient extends Remote {
    String getSessionUserId() throws RemoteException;

    // Search Articles
    Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException, NotAuthorizedException;

    // Search Customers
    Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException, NotAuthorizedException;

    // Invoice
    InvoiceDTO findInvoiceById(Long id) throws RemoteException, NotAuthorizedException, InvoiceException;

    // Return
    void returnItem(LineItemDTO lineItem, int quantity) throws RemoteException, NotAuthorizedException, InvoiceException;

    // Shopping Cart
    void addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    void emptyShoppingCart() throws RemoteException, NotAuthorizedException;

    void buyFromShoppingCart(int customerId) throws RemoteException, NotAuthorizedException;

    ShoppingCartDTO getShoppingCart() throws RemoteException, NotAuthorizedException;

    // Messaging
    void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException;

    void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException;

    Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException;

    void acknowledgeMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException;

    Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException;

    // Authorization
    boolean isAuthorizedFor(RemoteFunctionPermission functionPermission) throws RemoteException;

    // Application Client
    void destroy() throws RemoteException;
}
