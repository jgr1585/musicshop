package at.fhv.teamd.musicshop.userclient.communication;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartSubject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

public class RemoteFacade implements ApplicationClient {
    private static final int REMOTE_PORT = Registry.REGISTRY_PORT;

    private static RemoteFacade instance;
    private static ApplicationClient applicationClient;

    private RemoteFacade() {
    }

    public static void authenticateSession(String host, String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        try {
            LocateRegistry.getRegistry(host, REMOTE_PORT);
            ApplicationClientFactory applicationClientFactory
                    = (ApplicationClientFactory) Naming.lookup("rmi://" + host + ":" + REMOTE_PORT + "/ApplicationClientFactoryImpl");

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

        throw new IllegalStateException("Application client unavailable yet.");
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws RemoteException, ApplicationClientException, NotAuthorizedException {
        return getApplicationClientOrThrow().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws RemoteException, CustomerDBClientException, NotAuthorizedException {
        return getApplicationClientOrThrow().searchCustomersByName(name);
    }

    @Override
    public boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException {
        boolean returnValue = getApplicationClientOrThrow().addToShoppingCart(mediumDTO, amount);
        if (returnValue) {
            ShoppingCartSubject.notifyShoppingCartUpdate();
        }
        return returnValue;
    }

    @Override
    public boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException {
        boolean returnValue = getApplicationClientOrThrow().removeFromShoppingCart(mediumDTO, amount);
        if (returnValue) {
            ShoppingCartSubject.notifyShoppingCartUpdate();
        }
        return returnValue;
    }

    @Override
    public void emptyShoppingCart() throws RemoteException, NotAuthorizedException {
        getApplicationClientOrThrow().emptyShoppingCart();
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws RemoteException, NotAuthorizedException {
        boolean returnValue = getApplicationClientOrThrow().buyFromShoppingCart(customerId);
        if (returnValue) {
            ShoppingCartSubject.notifyShoppingCartUpdate();
        }
        return returnValue;
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().getShoppingCart();
    }

    @Override
    public boolean publishOrder(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().publishOrder(mediumDTO, quantity);
    }

    @Override
    public boolean publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().publishMessage(message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().receiveMessages();
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().getAllTopics();
    }

    @Override
    public void destroy() throws RemoteException {
        getApplicationClientOrThrow().destroy();
        applicationClient = null;
    }
}
