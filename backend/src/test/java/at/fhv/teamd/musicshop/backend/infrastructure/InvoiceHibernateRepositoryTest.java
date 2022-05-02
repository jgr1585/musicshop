package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

class InvoiceHibernateRepositoryTest {
    private InvoiceHibernateRepository invoiceHibernateRepository;

    @BeforeEach
    void init() {
        invoiceHibernateRepository = new InvoiceHibernateRepository();
    }

    @Test
    void given_invoiceRepository_when_createInvoice_then_contains_Invoice() {
        // given
        Medium medium = BaseRepositoryData.getMedia().iterator().next();
        Invoice invoice = Invoice.of(Set.of(new LineItem(Quantity.of(2), medium)));

        // when
        this.invoiceHibernateRepository.addInvoice(invoice);

        // then
        Assertions.assertDoesNotThrow(() -> this.invoiceHibernateRepository.findInvoiceById(invoice.getId()));
    }

    @Test
    void given_invoiceRepository_when_findInvoiceById_returnInvoiceOrEmpty() {
        // given
        Invoice invoice = BaseRepositoryData.getInvoices().stream().iterator().next();

        // when .. then
        Assertions.assertEquals(invoice, this.invoiceHibernateRepository.findInvoiceById(invoice.getId()).get());
        Assertions.assertEquals(Optional.empty(), this.invoiceHibernateRepository.findInvoiceById(0L));
    }

    @Test
    void given_invoiceRepository_when_findInvoiceByLineItemId_returnInvoiceOrEmpty() {
        // given
        Invoice invoice = BaseRepositoryData.getInvoices().stream().iterator().next();

        // when .. then
        LineItem lineItem = invoice.getLineItems().iterator().next();
        Assertions.assertEquals(invoice, this.invoiceHibernateRepository.findInvoiceByLineItemId(lineItem.getId()).get());
        Assertions.assertEquals(Optional.empty(), this.invoiceHibernateRepository.findInvoiceByLineItemId(0L));
    }

    @Test
    void given_invoiceRepository_when_update_then_updateInvoice() {
        // given
        Invoice expectedInvoice = BaseRepositoryData.getInvoices().stream().iterator().next();

        // when
        expectedInvoice.getLineItems().iterator().next().increaseQuantityReturned(Quantity.of(1));

        // then
        Invoice actualInvoice = this.invoiceHibernateRepository.findInvoiceById(expectedInvoice.getId()).get();
        Assertions.assertEquals(expectedInvoice, actualInvoice);
    }
}