package at.fhv.teamd.musicshop.backend.application.services.rest.auth;

import at.fhv.teamd.musicshop.backend.communication.CustomerClient;
import at.fhv.teamd.musicshop.library.CustomerDBClient;
import at.fhv.teamd.musicshop.library.exceptions.CustomerDBClientException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.rmi.RemoteException;

@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private Customer authenticatedUser;

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String username) throws CustomerDBClientException, RemoteException {
        this.authenticatedUser = searchCustomersByName(username);
    }

}
