package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;

import java.util.Optional;

public interface InvoiceRepository {
    void addInvoice(Invoice invoice);
    Optional<Invoice> findInvoiceById(Long id);
    Optional<Invoice> findInvoiceByLineItemId(Long lineItemId);
    void update(Invoice invoice);
}
