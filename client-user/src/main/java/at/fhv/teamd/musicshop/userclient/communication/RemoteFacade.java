package at.fhv.teamd.musicshop.userclient.communication;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.rmi.Naming;
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
        try {
            LocateRegistry.getRegistry(REMOTE_HOST, REMOTE_PORT);
            ApplicationClientFactory applicationClientFactory
                    = (ApplicationClientFactory) Naming.lookup("rmi://"+REMOTE_HOST+"/ApplicationClientFactoryImpl");

            applicationClient = applicationClientFactory.createApplicationClient();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Cannot connect to server (may be offline).", ButtonType.CLOSE).showAndWait();
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static RemoteFacade getInstance() {
        if (instance == null) {
            instance = new RemoteFacade();
        }

        return instance;
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException {
        return applicationClient.searchArticlesByAttributes(title, artist);
    }

    @Override
    public void addToShoppingCart(ArticleDTO articleDTO, MediumDTO analogMediumDTO, int amount) throws RemoteException {
        applicationClient.addToShoppingCart(articleDTO, analogMediumDTO, amount);
    }

    @Override
    public void removeFromShoppingCart(MediumDTO analogMediumDTO, int amount) throws RemoteException {
        applicationClient.removeFromShoppingCart(analogMediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws RemoteException {
        applicationClient.emptyShoppingCart();
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws RemoteException {
        return applicationClient.getShoppingCart();
    }
}
