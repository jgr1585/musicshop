package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;

import java.util.Optional;
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
}
