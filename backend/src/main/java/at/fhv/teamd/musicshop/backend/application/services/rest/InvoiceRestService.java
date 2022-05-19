package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

// TODO: Use JWT to identify user identity and remove userId params from request attributes
@Path("/invoice")
@Consumes("application/json")
@Produces("application/json")
public class InvoiceRestService {

    public InvoiceRestService() {}

    @GET
    public List<InvoiceDTO> getInvoices(@QueryParam("customerNo") int customerNo) {
       return ServiceFactory.getInvoiceServiceInstance().getInvoices(customerNo);
    }

    @GET
    @Path("/{invoiceId}")
    public List<AlbumDTO> getInvoiceAlbums(@PathParam("invoiceId") long invoiceId) {
        // TODO: verify that user is accessing its own invoices (customerNo)
        return ServiceFactory.getInvoiceServiceInstance().getInvoiceAlbums(invoiceId);
    }

    @GET
    @Path("/{invoiceId}/download/{albumId}")
    public String[] getInvoiceAlbumDownloadUrls(@Context UriInfo uriInfo, @PathParam("invoiceId") long invoiceId, @PathParam("albumId") long albumId) {
        // TODO: verify that user is accessing its own invoices and downloads (customerNo)
        String baseUri = uriInfo.getBaseUri().toString();
        String baserUriTrailedSlash = baseUri.substring(0, baseUri.lastIndexOf('/'));
        String baseUriWithoutApplicationPath = baserUriTrailedSlash.substring(0, baserUriTrailedSlash.lastIndexOf('/')) + '/';
        return ServiceFactory.getInvoiceServiceInstance().getInvoiceAlbumDownloadUrls(baseUriWithoutApplicationPath, invoiceId, albumId);
    }
}
