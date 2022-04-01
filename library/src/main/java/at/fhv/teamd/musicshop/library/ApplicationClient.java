package at.fhv.teamd.musicshop.library;

import at.fhv.teamd.musicshop.library.DTO.AnalogMediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ApplicationClient extends Remote {
    // Search Articles
    List<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException;

    // Shopping Cart
    void addToShoppingCart(ArticleDTO articleDTO, AnalogMediumDTO analogMediumDTO, int amount) throws RemoteException;

    void removeFromShoppingCart(AnalogMediumDTO analogMediumDTO, int amount) throws RemoteException;

    void emptyShoppingCart() throws RemoteException;

    ShoppingCartDTO getShoppingCart() throws RemoteException;
}
