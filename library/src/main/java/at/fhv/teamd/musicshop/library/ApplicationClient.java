package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;

import javax.naming.NamingException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public interface ApplicationClient extends Remote {
    // Search Articles
    Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException, NotAuthorizedException;

    // Search Customer for Invoice
    Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException, NotAuthorizedException;

    // Shopping Cart
    boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException;

    void emptyShoppingCart() throws RemoteException, NotAuthorizedException;

    boolean buyFromShoppingCart(int customerId) throws RemoteException, NotAuthorizedException;

    ShoppingCartDTO getShoppingCart() throws RemoteException, NotAuthorizedException;

    boolean publishMessage(String topic, String title, String message) throws RemoteException, NotAuthorizedException;

    // Application Client
    void destroy() throws RemoteException;
}
