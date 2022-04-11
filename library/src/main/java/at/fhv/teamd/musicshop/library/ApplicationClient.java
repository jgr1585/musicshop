package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ApplicationClient extends Remote {
    // Search Articles
    Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException;

    // Search Customer for Invoice
    Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException;

    // Shopping Cart
    boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException;

    boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException;

    void emptyShoppingCart() throws RemoteException;

    boolean buyFromShoppingCart(int customerId) throws RemoteException;

    ShoppingCartDTO getShoppingCart() throws RemoteException;
}
