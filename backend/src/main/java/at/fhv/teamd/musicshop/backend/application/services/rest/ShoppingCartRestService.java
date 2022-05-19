package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.forms.AddToShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.BuyFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.EmptyShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.RemoveFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;

import javax.ws.rs.*;

// TODO: Use JWT to identify user identity and remove userId params from request attributes
@Path("/shoppingcart")
@Consumes("application/json")
@Produces("application/json")
public class ShoppingCartRestService {
    public ShoppingCartRestService() {}

    @POST
    @Path("/add")
    public void addToShoppingCart(AddToShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(form.userId, form.mediumId, form.amount);
    }

    @POST
    @Path("/remove")
    public void removeFromShoppingCart(RemoveFromShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(form.userId, form.mediumId, form.amount);
    }

    @POST
    @Path("/empty")
    public void emptyShoppingCart(EmptyShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(form.userId);
    }

    @GET
    @Path("/get")
    public ShoppingCartDTO getShoppingCart(@QueryParam("userId") String userId) {
        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(userId);
    }

    @POST
    @Path("/buy")
    public void buyFromShoppingCart(BuyFromShoppingCartForm form) {
        ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(form.userId, form.customerId);
    }
}
