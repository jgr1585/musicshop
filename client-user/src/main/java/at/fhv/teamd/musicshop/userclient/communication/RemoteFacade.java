package at.fhv.teamd.musicshop.userclient.communication;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.dto.*;
import at.fhv.teamd.musicshop.library.exceptions.*;
import at.fhv.teamd.musicshop.library.permission.RemoteFunctionPermission;
import at.fhv.teamd.musicshop.userclient.observer.ReturnSubject;
import at.fhv.teamd.musicshop.userclient.observer.ShoppingCartSubject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.Set;

public class RemoteFacade implements ApplicationClient {

    private static RemoteFacade instance;
    private static ApplicationClient applicationClient;

    private RemoteFacade() {
    }

    public static void authenticateSession(String host, String authUser, String authPassword) throws AuthenticationFailedException {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://" + host + ":8080");
            Context ctx = new InitialContext(props);

            // ejb:/[DeployedName]/Implementierungsname![packages + Interface of Bean]
            applicationClient = (ApplicationClient) ctx.lookup("ejb:/backend-1.0-SNAPSHOT/ApplicationClientImpl!at.fhv.teamd.musicshop.library.ApplicationClient");

            applicationClient.authenticate(authUser, authPassword);

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static RemoteFacade getInstance() {
        if (instance == null) {
            instance = new RemoteFacade();
        }
        return instance;
    }

    private ApplicationClient getApplicationClientOrThrow() {
        if (applicationClient != null) {
            return applicationClient;
        }

        throw new IllegalStateException("Application client unavailable yet.");
    }

    @Override
    public void authenticate(String authUser, String authPassword) {
        //TODO: implement
    }

    @Override
    public String getSessionUserId() {
        return getApplicationClientOrThrow().getSessionUserId();
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        return getApplicationClientOrThrow().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, NotAuthorizedException {
        return getApplicationClientOrThrow().searchCustomersByName(name);
    }

    @Override
    public InvoiceDTO findInvoiceById(Long id) throws NotAuthorizedException, InvoiceException {
        return getApplicationClientOrThrow().findInvoiceById(id);
    }

    @Override
    public void returnItem(LineItemDTO lineItem, int quantity) throws NotAuthorizedException, InvoiceException {
        getApplicationClientOrThrow().returnItem(lineItem, quantity);
        ReturnSubject.notifyReturnUpdate();
    }

    @Override
    public void addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        getApplicationClientOrThrow().addToShoppingCart(mediumDTO, amount);
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public void removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException, ShoppingCartException {
        getApplicationClientOrThrow().removeFromShoppingCart(mediumDTO, amount);
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        getApplicationClientOrThrow().emptyShoppingCart();
        ShoppingCartSubject.notifyShoppingCartUpdate();
    }

    @Override
    public String buyFromShoppingCart(int customerId) throws NotAuthorizedException, ShoppingCartException {
        String invoiceNo;
        invoiceNo = getApplicationClientOrThrow().buyFromShoppingCart(customerId);
        ShoppingCartSubject.notifyShoppingCartUpdate();
        return invoiceNo;
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException, ShoppingCartException {
        return getApplicationClientOrThrow().getShoppingCart();
    }

    @Override
    public void publishOrderMessage(MediumDTO mediumDTO, String quantity) throws NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().publishOrderMessage(mediumDTO, quantity);
    }

    @Override
    public void publishMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().publishMessage(message);
    }

    @Override
    public Set<MessageDTO> receiveMessages() throws NotAuthorizedException, MessagingException {
        return getApplicationClientOrThrow().receiveMessages();
    }

    @Override
    public void acknowledgeMessage(MessageDTO message) throws NotAuthorizedException, MessagingException {
        getApplicationClientOrThrow().acknowledgeMessage(message);
    }

    @Override
    public Set<TopicDTO> getAllTopics() throws NotAuthorizedException {
        return getApplicationClientOrThrow().getAllTopics();
    }

    @Override
    public boolean isAuthorizedFor(RemoteFunctionPermission functionPermission) {
        return getApplicationClientOrThrow().isAuthorizedFor(functionPermission);
    }

    public static void destroy() {
        applicationClient = null;
    }
}
