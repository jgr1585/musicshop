package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildInvoiceDTO;

public class InvoiceService {
    private static InvoiceRepository invoiceRepository;

    InvoiceService() {
        invoiceRepository = RepositoryFactory.getInvoiceRepositoryInstance();
    }

    public Long createInvoice(Set<LineItem> lineItems, int assignedCustomer) {
        Long id;
        if (assignedCustomer != 0) {
            id = invoiceRepository.addInvoice(Invoice.of(lineItems, assignedCustomer));
        } else {
            id = invoiceRepository.addInvoice(Invoice.of(lineItems));
        }
        System.out.println("created new invoice");
        return id;
    }

    public InvoiceDTO searchInvoiceById(Long id) throws InvoiceException {
        Optional<Invoice> invoiceOpt = invoiceRepository.findInvoiceById(id);
        if (invoiceOpt.isPresent()) {
            return buildInvoiceDTO(invoiceOpt.get());
        } else {
            throw new InvoiceException("Invoice not found");
        }
    }

    public void returnItem(LineItemDTO lineItem, int quantity) throws InvoiceException {
        Optional<Invoice> invoiceOpt = invoiceRepository.findInvoiceByLineItemId(lineItem.id());

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            invoice.getLineItems()
                    .stream()
                    .filter(lineItem1 -> lineItem1.getId() == lineItem.id())
                    .findFirst()
                    .orElseThrow(() -> new InvoiceException("LineItem not found"))
                    .increaseQuantityReturned(Quantity.of(quantity));
            invoiceRepository.update(invoice);
        } else {
            throw new InvoiceException("Invoice not found");
        }
    }

    public List<InvoiceDTO> getInvoices(int customerNo) {
        return invoiceRepository.findInvoicesByCustomerNo(customerNo).stream()
                .map(DTOProvider::buildInvoiceDTO)
                .collect(Collectors.toList());
    }

    public List<AlbumDTO> getInvoiceAlbums(long invoiceId) {
        return invoiceRepository.findInvoiceById(invoiceId).get().getLineItems().stream()
                .map(li -> li.getMedium().getAlbum())
                .map(DTOProvider::buildArticleDTO)
                .map(ar -> (AlbumDTO) ar)
                .collect(Collectors.toList());
    }

    public String[] getInvoiceAlbumDownloadUrls(String baseUri, long invoiceId, long albumId) {
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId).get();
        Album album = invoice.getLineItems().stream()
                .flatMap(li -> Stream.of(li.getMedium().getAlbum()))
                .filter(a -> a.getId() == albumId)
                .findFirst()
                .get();

        return album.getSongs().stream()
                .map(Article::getId)
                .map(id -> baseUri + "media/songs?id=" + id)
                .toArray(String[]::new);
    }
}
