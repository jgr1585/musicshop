package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;

// TODO: ensure that user is customer (only invoices from this user are shown)
@Path("/invoice")
public class InvoiceRestService {

    public InvoiceRestService() {}

    @GET
    @Consumes("text/json")
    public List<InvoiceDTO> getInvoices() {
       // return ServiceFactory.getInvoiceServiceInstance().getInvoices();
        return null;
    }

    @GET
    @Path("/{invoiceId}")
    @Consumes("text/json")
    public List<MediumDTO> getInvoiceMediums(@PathParam("invoiceId") long invoiceId) {
        return ServiceFactory.getInvoiceServiceInstance().getInvoiceMediums(invoiceId);
    }

    @GET
    @Path("/{invoiceId}/download/{mediumId}")
    @Consumes("text/json")
    public String[] getInvoiceMediumDownloadUrls(@Context UriInfo uriInfo, @PathParam("invoiceId") long invoiceId, @PathParam("mediumId") long mediumId) {
        return ServiceFactory.getInvoiceServiceInstance().getInvoiceMediumDownloadUrls(uriInfo.getBaseUri().toString(), invoiceId, mediumId);
    }
}
