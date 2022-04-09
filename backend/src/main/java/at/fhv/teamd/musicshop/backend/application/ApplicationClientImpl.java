package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.UUID;

public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {
    private final UUID sessionUUID;

    public ApplicationClientImpl() throws RemoteException {
        super();
        sessionUUID = UUID.randomUUID();
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException {
        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException {
        return ServiceFactory.getCustomerServiceInstance().searchCustomersByName(name);
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) {
        ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(sessionUUID, mediumDTO, amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) {
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(sessionUUID, mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() {
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(sessionUUID);
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws RemoteException {
        return ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(sessionUUID, customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() {
        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(sessionUUID);
    }
}
