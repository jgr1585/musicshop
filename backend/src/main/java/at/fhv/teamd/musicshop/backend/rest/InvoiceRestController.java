package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.InvoiceService;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedInvoiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Secured
@Path("/invoice")
@Consumes("application/json")
@Produces("application/json")
@SecurityRequirement(name = "Authentication")
public class InvoiceRestController {

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    private InvoiceService invoiceService = ServiceFactory.getInvoiceServiceInstance();

    public InvoiceRestController() {}

    @GET
    @Operation(summary = "Get customer invoices")
    @ApiResponse(responseCode = "200", description = "Returns List<InvoiceDTO>")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response getInvoices() {
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }

        try {
            return Response.ok(invoiceService.getInvoices(authenticatedUser.name())).build();
        } catch (InvoiceException e) {
            return Response.status(404, "Customer with username not found").build();
        }
    }

    @GET
    @Path("/{invoiceId}")
    @Operation(summary = "Get customer invoice")
    @ApiResponse(responseCode = "200", description = "Returns List<AlbumDTO>")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Invoice not found")
    public Response getInvoiceAlbums(@PathParam("invoiceId") long invoiceId) {
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }

        try {
            return Response.ok(invoiceService.getInvoiceAlbums(authenticatedUser.name(), invoiceId)).build();
        } catch (UnauthorizedInvoiceException e) {
            return Response.status(401, e.getMessage()).build();
        } catch (InvoiceException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }

    @GET
    @Path("/{invoiceId}/download-album/{albumId}")
    @Operation(summary = "Get customer album download URLs")
    @ApiResponse(responseCode = "200", description = "Returns String")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Invoice or album not found")
    public Response getInvoiceAlbumDownloadUrl(@Context UriInfo uriInfo, @PathParam("invoiceId") long invoiceId, @PathParam("albumId") long albumId) {
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }

        try {
            String baseUri = uriInfo.getBaseUri().toString();
            String baserUriTrailedSlash = baseUri.substring(0, baseUri.lastIndexOf('/'));
            String baseUriWithoutApplicationPath = baserUriTrailedSlash.substring(0, baserUriTrailedSlash.lastIndexOf('/')) + '/';

            String albumUrl = invoiceService.getInvoiceAlbumDownloadUrl(authenticatedUser.name(), baseUriWithoutApplicationPath, invoiceId, albumId);

            return Response.ok(singlePropertyJson("albumUrl", albumUrl)).build();
        } catch (UnauthorizedInvoiceException e) {
            return Response.status(401, e.getMessage()).build();
        } catch (InvoiceException e) {
            return Response.status(404, e.getMessage()).build();
        } catch (JsonProcessingException e) {
            return Response.status(500).build();
        }
    }

    private String singlePropertyJson(String propertyName, String value) throws JsonProcessingException {
        // create `ObjectMapper` instance
        ObjectMapper mapper = new ObjectMapper();

        // create a JSON object
        ObjectNode json = mapper.createObjectNode();
        json.put(propertyName, value);

        // convert `ObjectNode` to pretty-print JSON
        // without pretty-print, use `user.toString()` method
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    @GET
    @Path("/{invoiceId}/download-song/{songId}")
    @Operation(summary = "Get customer song download URLs")
    @ApiResponse(responseCode = "200", description = "Returns String")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Invoice or song not found")
    public Response getInvoiceSongDownloadUrl(@Context UriInfo uriInfo, @PathParam("invoiceId") long invoiceId, @PathParam("songId") long songId) {
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }

        try {
            String baseUri = uriInfo.getBaseUri().toString();
            String baserUriTrailedSlash = baseUri.substring(0, baseUri.lastIndexOf('/'));
            String baseUriWithoutApplicationPath = baserUriTrailedSlash.substring(0, baserUriTrailedSlash.lastIndexOf('/')) + '/';

            String songUrl = invoiceService.getInvoiceSongDownloadUrl(authenticatedUser.name(), baseUriWithoutApplicationPath, invoiceId, songId);

            return Response.ok(singlePropertyJson("songUrl", songUrl)).build();
        } catch (UnauthorizedInvoiceException e) {
            return Response.status(401, e.getMessage()).build();
        } catch (InvoiceException e) {
            return Response.status(404, e.getMessage()).build();
        } catch (JsonProcessingException e) {
            return Response.status(500).build();
        }
    }
}
