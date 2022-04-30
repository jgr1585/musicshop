package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.geom.QuadCurve2D;
import java.util.Set;

import static at.fhv.teamd.musicshop.backend.application.services.DTOProvider.buildInvoiceDTO;

public class InvoiceService {
    private static InvoiceRepository invoiceRepository;

    InvoiceService() {
        invoiceRepository = RepositoryFactory.getInvoiceRepositoryInstance();
    }

    public void createInvoice(Set<LineItem> lineItems, int assignedCustomer) {
        if (assignedCustomer != 0) {
            invoiceRepository.addInvoice(Invoice.of(lineItems, assignedCustomer));
        } else {
            invoiceRepository.addInvoice(Invoice.of(lineItems));
        }
        System.out.println("created new invoice");
    }

    public InvoiceDTO searchInvoiceById(Long id) {
        return buildInvoiceDTO(invoiceRepository.findInvoiceById(id).orElseThrow());
    }

    public boolean returnItem(LineItemDTO lineItem, int quantity) {
        Invoice invoice = invoiceRepository.findInvoiceByLineItemId(lineItem.id());

        invoice.getLineItems()
                .stream()
                .filter(lineItem1 -> lineItem1.getId() == lineItem.id())
                .findFirst()
                .orElseThrow()
                .increaseQuantityReturned(Quantity.of(quantity));

        invoiceRepository.update(invoice);
        return true;
    }
}
