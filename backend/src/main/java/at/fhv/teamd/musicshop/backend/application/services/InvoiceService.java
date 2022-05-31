package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.CustomerRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedInvoiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.camel.json.simple.JsonObject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public List<InvoiceDTO> getInvoices(String username) throws InvoiceException, CustomerNotFoundException {
        return invoiceRepository.findInvoicesByCustomerNo(
                        ServiceFactory
                                .getCustomerServiceInstance()
                                .getCustomerNoByUsername(username)).stream()
                .map(DTOProvider::buildInvoiceDTO)
                .collect(Collectors.toList());
    }

    private Song getSong(String username, long invoiceId, long songId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        int customerNo = ServiceFactory.getCustomerServiceInstance().getCustomerNoByUsername(username);

        // get invoice
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId)
                .orElseThrow(() -> new InvoiceException("Invoice not found."));

        // check if invoice is an invoice for customer
        if (!invoice.getCustomerNo().equals(customerNo)) {
            throw UnauthorizedInvoiceException.invoiceAccess();
        }

        return invoice.getLineItems().stream()
                .flatMap(li -> li.getMedium().getAlbum().getSongs().stream())
                .filter(s -> s.getId() == songId)
                .findFirst()
                .orElseThrow(UnauthorizedInvoiceException::invoiceNotContainsSong);

    }

    private Album getAlbum(String username, long invoiceId, long albumId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        int customerNo = ServiceFactory.getCustomerServiceInstance().getCustomerNoByUsername(username);

        // get invoice
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId)
                .orElseThrow(() -> new InvoiceException("Invoice not found."));

        // check if invoice is an invoice for customer
        if (!invoice.getCustomerNo().equals(customerNo)) {
            throw UnauthorizedInvoiceException.invoiceAccess();
        }

        return invoice.getLineItems().stream()
                .flatMap(li -> Stream.of(li.getMedium().getAlbum()))
                .filter(a -> a.getId() == albumId)
                .findFirst()
                .orElseThrow(UnauthorizedInvoiceException::invoiceNotContainsAlbum);
    }


    public SongDTO getSongDTO(String username, long invoiceId, long songId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        return DTOProvider.buildArticleDTO(getSong(username, invoiceId, songId));
    }

    public AlbumDTO getAlbumDTO(String username, long invoiceId, long albumId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        return DTOProvider.buildArticleDTO(getAlbum(username, invoiceId, albumId));
    }

    public List<AlbumDTO> getInvoiceAlbums(String username, long invoiceId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        int customerNo = ServiceFactory.getCustomerServiceInstance().getCustomerNoByUsername(username);

        // get invoice
        Invoice invoice = invoiceRepository.findInvoiceById(invoiceId)
                .orElseThrow(() -> new InvoiceException("Invoice not found."));

        // check if invoice is an invoice for customer
        if (!invoice.getCustomerNo().equals(customerNo)) {
            throw UnauthorizedInvoiceException.invoiceAccess();
        }

        return invoice.getLineItems().stream()
                .map(li -> li.getMedium().getAlbum())
                .map(DTOProvider::buildArticleDTO)
                .collect(Collectors.toList());
    }

    public String getInvoiceAlbumDownloadUrl(String username, String baseUri, long invoiceId, long albumId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        Album album = getAlbum(username, invoiceId, albumId);

        return baseUri + "media/album?invoiceId=" + invoiceId + "&id=" + album.getId();
    }

    public String getInvoiceSongDownloadUrl(String username, String baseUri, long invoiceId, long songId) throws InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        Song song = getSong(username, invoiceId, songId);

        return baseUri + "media/song?invoiceId=" + invoiceId + "&id=" + song.getId();
    }
}
