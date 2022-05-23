package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.forms.AddToShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.BuyFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.RemoveFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.application.services.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.application.services.rest.auth.Secured;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.*;

@Secured
@Path("/shoppingcart")
@Consumes("application/json")
@Produces("application/json")
@SecurityRequirement(name = "Authentication")
public class ShoppingCartRestService {

    @AuthenticatedUser
    String authenticatedUser;

    public ShoppingCartRestService() {
    }

    @POST
    @Path("/add")
    public void addToShoppingCart(AddToShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(authenticatedUser, form.mediumId, form.amount);
    }

    @POST
    @Path("/remove")
    public void removeFromShoppingCart(RemoveFromShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(authenticatedUser, form.mediumId, form.amount);
    }

    @POST
    @Path("/empty")
    public void emptyShoppingCart() {
        System.out.println("authenticatedUser: " + authenticatedUser);
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(authenticatedUser);
    }

    @POST
    @Path("/get")
    public ShoppingCartDTO getShoppingCart() {
        System.out.println("authenticatedUser: " + authenticatedUser);
        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(authenticatedUser);
    }

    @POST
    @Path("/buy")
    public void buyFromShoppingCart(BuyFromShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(authenticatedUser, form.customerId);
    }
}
