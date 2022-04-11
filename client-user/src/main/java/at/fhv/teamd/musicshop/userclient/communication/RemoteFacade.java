package at.fhv.teamd.musicshop.userclient.communication;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

public class RemoteFacade implements ApplicationClient {
    private static final String REMOTE_HOST = "localhost";
    private static final int REMOTE_PORT = Registry.REGISTRY_PORT;

    private static RemoteFacade instance;
    private static ApplicationClient applicationClient;

    private RemoteFacade() {
    }

    public static void authenticateSession(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        try {
            LocateRegistry.getRegistry(REMOTE_HOST, REMOTE_PORT);
            ApplicationClientFactory applicationClientFactory
                    = (ApplicationClientFactory) Naming.lookup("rmi://" + REMOTE_HOST + ":" + REMOTE_PORT + "/ApplicationClientFactoryImpl");

            applicationClient = applicationClientFactory.createApplicationClient(authUser, authPassword);

        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static RemoteFacade getInstance() throws RemoteException {
        if (instance == null) {
            instance = new RemoteFacade();
        }

        return instance;
    }

    private ApplicationClient getApplicationClientOrThrow() throws RemoteException {
        if (applicationClient != null) {
            return applicationClient;
        }

        throw new IllegalStateException("FUCK OFF");
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException {
        return getApplicationClientOrThrow().searchArticlesByAttributes(title, artist);
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException {
        getApplicationClientOrThrow().addToShoppingCart(mediumDTO, amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException {
        getApplicationClientOrThrow().removeFromShoppingCart(mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws RemoteException {
        getApplicationClientOrThrow().emptyShoppingCart();
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws RemoteException {
        return getApplicationClientOrThrow().buyFromShoppingCart(customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws RemoteException {
        return getApplicationClientOrThrow().getShoppingCart();
    }
}
