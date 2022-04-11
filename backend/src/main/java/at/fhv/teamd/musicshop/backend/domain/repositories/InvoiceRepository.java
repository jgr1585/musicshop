package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;

public interface InvoiceRepository {
    void addInvoice(Invoice invoice);
}
