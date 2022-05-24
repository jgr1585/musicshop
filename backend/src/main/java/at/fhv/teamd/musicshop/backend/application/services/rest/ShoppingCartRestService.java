package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.forms.AddToShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.BuyFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.forms.RemoveFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.application.services.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.application.services.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.application.services.rest.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.NoSuchElementException;

@Secured
@Path("/shoppingcart")
@Consumes("application/json")
@Produces("application/json")
@SecurityRequirement(name = "Authentication")
public class ShoppingCartRestService {

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    public ShoppingCartRestService() {
    }

    @POST
    @Path("/add")
    @Operation(summary = "Add an item to your shoppingcart")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    @ApiResponse(responseCode = "404", description = "Article not found")
    @ApiResponse(responseCode = "409", description = "Quantity not acceptable")
    public Response addToShoppingCart(AddToShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        try {
            ServiceFactory.getShoppingCartServiceInstance().addToShoppingCart(authenticatedUser.name(), form.mediumId, form.amount);
        } catch (NoSuchElementException e) {
            return Response.status(404, "Article not found").build();
        } catch (IllegalArgumentException e) {
            return Response.status(409, "Quantity not acceptable").build();
        }
        return Response.status(204).build();
    }

    @POST
    @Path("/remove")
    @Operation(summary = "Remove  an item to your shoppingcart")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400", description = "No params provided")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    public Response removeFromShoppingCart(RemoveFromShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        // TODO: implement 400
        ServiceFactory.getShoppingCartServiceInstance().removeFromShoppingCart(authenticatedUser.name(), form.mediumId, form.amount);
        return Response.status(204).build();
    }

    @POST
    @Path("/empty")
    @Operation(summary = "Empty your shoppingcart")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    public Response emptyShoppingCart() {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        ServiceFactory.getShoppingCartServiceInstance().emptyShoppingCart(authenticatedUser.name());
        return Response.status(204).build();
    }

    @POST
    @Path("/get")
    @Operation(summary = "Query your shoppingcart")
    @ApiResponse(responseCode = "200", description = "Returns ShoppingCartDTO")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    public Response getShoppingCart() {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        return Response.ok(ServiceFactory.getShoppingCartServiceInstance().getShoppingCart(authenticatedUser.name())).build();
    }

    @POST
    @Path("/buy")
    @Operation(summary = "Buy Items from shoppingcart",
            description = "Returns the InvoiceNo. on success?")
    @ApiResponse(responseCode = "200", description = "Successfully bought Items")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @ApiResponse(responseCode = "409", description = "No lineItems in shoppingcart")
    @ApiResponse(responseCode = "409", description = "Not enough items in stock")
    public Response buyFromShoppingCart(BuyFromShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        // TODO: implement 404
        String invoiceNo;
        try {
            invoiceNo = ServiceFactory.getShoppingCartServiceInstance().buyFromShoppingCart(authenticatedUser.name(), form.customerId);
        } catch (IllegalArgumentException e) {
            return Response.status(409, "No lineItems in shoppingcart").build();
        } catch (RuntimeException e) {
            return Response.status(409, "Not enough items in stock").build();
        }
        return Response.ok("Invoice created with No: " + invoiceNo).build();
    }
}
