package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;

import javax.ws.rs.*;

@Path("/shoppingcart")
public class ShoppingCartRestService {
    public ShoppingCartRestService() {}

    @POST
    @Path("/add")
    @Consumes("text/json")
    public void addToShoppingCart(@QueryParam("userId") String userId, MediumDTO mediumDTO, @QueryParam("amount") int amount) {
        ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(userId, mediumDTO, amount);
    }

    @POST
    @Path("/remove")
    @Consumes("text/json")
    public void removeFromShoppingCart(@QueryParam("userId") String userId, MediumDTO mediumDTO, @QueryParam("amount") int amount) {
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(userId, mediumDTO, amount);
    }

    @GET
    @Path("/empty")
    @Consumes("text/json")
    public void emptyShoppingCart(@QueryParam("userId") String userId) {
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(userId);
    }

    @GET
    @Path("/get")
    @Produces("text/json")
    @Consumes("text/json")
    public ShoppingCartDTO getShoppingCart(@QueryParam("userId") String userId) {
        return ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(userId);
    }

    @GET
    @Path("/buy")
    @Produces("text/json")
    @Consumes("text/json")
    public void buyFromShoppingCart(@QueryParam("userId") String userId, @QueryParam("id") int id) {
        ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(userId, id);
    }
}
