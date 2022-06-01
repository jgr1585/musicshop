package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.rest.forms.AddToShoppingCartForm;
import at.fhv.teamd.musicshop.backend.rest.forms.BuyFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.rest.forms.RemoveFromShoppingCartForm;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.NoSuchElementException;

@Secured
@Path("/shoppingcart")
@Consumes("application/json")
@Produces("application/json")
@SecurityRequirement(name = "Authentication")
@NoArgsConstructor
public class ShoppingCartRestController {

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    @POST
    @Path("/add")
    @Operation(summary = "Add an item to your ShoppingCart")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    @ApiResponse(responseCode = "404", description = "Article not found")
    @ApiResponse(responseCode = "406", description = "Already in Cart")
    public Response addToShoppingCart(AddToShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        try {
            ServiceFactory.getShoppingCartServiceInstance().addDigitalsToShoppingCart(authenticatedUser.name(), form.getMediumId());
        } catch (NoSuchElementException e) {
            return Response.status(404, e.getMessage()).build();
        } catch (ShoppingCartException e) {
            return Response.status(406, e.getMessage()).build();
        }
        return Response.status(204).build();
    }

    @POST
    @Path("/remove")
    @Operation(summary = "Remove  an item to your ShoppingCart")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400", description = "No params provided")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    public Response removeFromShoppingCart(RemoveFromShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        try {
            ServiceFactory.getShoppingCartServiceInstance().removeDigitalsFromShoppingCart(authenticatedUser.name(), form.getMediumId());
        } catch (ShoppingCartException e) {
            return Response.status(406, e.getMessage()).build();
        }
        return Response.status(204).build();
    }

    @POST
    @Path("/empty")
    @Operation(summary = "Empty your ShoppingCart")
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
    @Operation(summary = "Query your ShoppingCart")
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
    @Operation(summary = "Buy Items from ShoppingCart",
            description = "Returns the InvoiceNo. on success?")
    @ApiResponse(responseCode = "200", description = "Successfully bought Items")
    @ApiResponse(responseCode = "401", description = "Not Authorized")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @ApiResponse(responseCode = "406", description = "No lineItems in ShoppingCart")
    @ApiResponse(responseCode = "406", description = "CreditCardNo does not match")
    @ApiResponse(responseCode = "406", description = "Only digital mediums allowed")
    public Response buyFromShoppingCart(BuyFromShoppingCartForm form) {
        System.out.println("authenticatedUser: " + authenticatedUser.name());
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }
        String invoiceNo;
        try {
            invoiceNo = ServiceFactory.getShoppingCartServiceInstance().buyDigitalsFromShoppingCart(authenticatedUser.name(), form.getCreditCardNo());
        } catch (CustomerNotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        } catch (ShoppingCartException e) {
            return Response.status(406, e.getMessage()).build();
        }
        return Response.ok("Invoice created with No: " + invoiceNo).build();
    }
}
