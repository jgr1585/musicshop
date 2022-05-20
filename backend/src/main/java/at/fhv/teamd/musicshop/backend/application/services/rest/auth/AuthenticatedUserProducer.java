package at.fhv.teamd.musicshop.backend.application.services.rest.auth;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@RequestScoped
public class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private String authenticatedUser;

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String username) {
        this.authenticatedUser = username;
    }

}
