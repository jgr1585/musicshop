package at.fhv.teamd.musicshop.backend.application;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.domain.user.UserRole;
import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.CustomerDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;
import at.fhv.teamd.musicshop.library.exceptions.AuthenticationFailedException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class ApplicationClientImpl extends UnicastRemoteObject implements ApplicationClient {
    private final AuthService authService;
    private final String userId;

    private ApplicationClientImpl(String userId) throws RemoteException {
        super();
        authService = ServiceFactory.getAuthServiceInstance();
        this.userId = userId;
    }

    public static ApplicationClientImpl newInstance(String authUser, String authPassword) throws RemoteException, AuthenticationFailedException {
        AuthService.authenticate(authUser, authPassword);

        return new ApplicationClientImpl(authUser);
    }

    @Override
    public Set<ArticleDTO> searchArticlesByAttributes(String title, String artist) throws ApplicationClientException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }

    @Override
    public Set<CustomerDTO> searchCustomersByName(String name) throws CustomerDBClientException, RemoteException, NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getCustomerServiceInstance().searchCustomersByName(name);
    }

    @Override
    public boolean addToShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.ADMIN); // TODO: For presentation/demonstration purposes only (normally only requires SELLER)

        return ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(userId, mediumDTO, amount);
    }

    @Override
    public boolean removeFromShoppingCart(MediumDTO mediumDTO, int amount) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(userId, mediumDTO, amount);
    }

    @Override
    public void emptyShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(userId);
    }

    @Override
    public boolean buyFromShoppingCart(int customerId) throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(userId, customerId);
    }

    @Override
    public ShoppingCartDTO getShoppingCart() throws NotAuthorizedException {
        authService.authorizeAccessLevel(UserRole.SELLER);

        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(userId);
    }

    @Override
    public void destroy() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(this, true);
    }
}
