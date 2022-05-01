package at.fhv.teamd.musicshop.userclient.communication;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.ApplicationClientFactory;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.observer.ReturnSubject;
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
    public String getSessionUserId() throws RemoteException {
        return getApplicationClientOrThrow().getSessionUserId();
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
    public InvoiceDTO findInvoiceById(Long id) throws RemoteException, NotAuthorizedException, InvoiceException {
        return getApplicationClientOrThrow().findInvoiceById(id);
    }

    @Override
    public void returnItem(LineItemDTO lineItem, int quantity) throws RemoteException, NotAuthorizedException, InvoiceException {
        getApplicationClientOrThrow().returnItem(lineItem, quantity);
        ReturnSubject.notifyReturnUpdate();
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException {
        getApplicationClientOrThrow().addToShoppingCart(mediumDTO, amount);
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws RemoteException, NotAuthorizedException {
        getApplicationClientOrThrow().removeFromShoppingCart(mediumDTO, amount);
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public void emptyShoppingCart() throws RemoteException, NotAuthorizedException {
        getApplicationClientOrThrow().emptyShoppingCart();
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public void buyFromShoppingCart(int customerId) throws RemoteException, NotAuthorizedException {
        getApplicationClientOrThrow().buyFromShoppingCart(customerId);
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().getShoppingCart();
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws RemoteException, NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().publishOrderMessage(mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().publishMessage(message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws RemoteException, NotAuthorizedException, MessagingException {
        return getApplicationClientOrThrow().receiveMessages();
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws RemoteException, NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().acknowledgeMessage(message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws RemoteException, NotAuthorizedException {
        return getApplicationClientOrThrow().getAllTopics();
    }

    @Override
    public boolean isAuthorizedFor(RemoteFunctionPermission functionPermission) throws RemoteException {
        return getApplicationClientOrThrow().isAuthorizedFor(functionPermission);
    }

    @Override
    public void destroy() throws RemoteException {
        getApplicationClientOrThrow().destroy();
        applicationClient = null;
    }
}
