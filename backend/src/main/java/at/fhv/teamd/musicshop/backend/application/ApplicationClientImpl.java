package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.AnalogMediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {
    private final UUID sessionUUID;

    public ApplicationClientImpl() throws RemoteException {
        super();
        sessionUUID = UUID.randomUUID();
    }

    @Override
    public List<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException {
        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }

    @Override
    public void addToShoppingCart(ArticleDTO articleDTO, AnalogMediumDTO analogMediumDTO, int amount) {
        ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(sessionUUID, articleDTO, analogMediumDTO, amount);
    }

    @Override
    public void removeFromShoppingCart(AnalogMediumDTO analogMediumDTO, int amount) {
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(sessionUUID, analogMediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() {
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(sessionUUID);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() {
        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(sessionUUID);
    }
}
