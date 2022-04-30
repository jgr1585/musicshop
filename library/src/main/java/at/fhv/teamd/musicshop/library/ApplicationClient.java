package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.MessagingException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
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
    InvoiceDTO findInvoiceById(Long id) throws RemoteException, NotAuthorizedException;

    // Return
    boolean returnItem(LineItemDTO lineItem, int quantity) throws RemoteException, NotAuthorizedException;

    // Shopping Cart
    boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    void emptyShoppingCart() throws RemoteException, NotAuthorizedException;

    boolean buyFromShoppingCart(int customerId) throws RemoteException, NotAuthorizedException;

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
