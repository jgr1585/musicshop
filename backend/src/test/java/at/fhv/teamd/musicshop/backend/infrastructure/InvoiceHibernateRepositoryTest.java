package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.medium.Stock;
import at.fhv.teamd.musicshop.backend.domain.medium.Supplier;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
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
        Supplier supplier = new Supplier("Test", Duration.ofDays(6));
        Medium medium = new Medium(BigDecimal.ONE, MediumType.CD, Stock.of(Quantity.of(1)), supplier, null);
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
        Supplier supplier = new Supplier("Test", Duration.ofDays(6));
        Medium medium = new Medium(BigDecimal.ONE, MediumType.CD, Stock.of(Quantity.of(1)), supplier, null);
        Invoice expectedInvoice = Invoice.of(Set.of(new LineItem(Quantity.of(2), medium)));

        this.invoiceHibernateRepository.addInvoice(expectedInvoice);

        // when
        expectedInvoice.getLineItems().iterator().next().increaseQuantityReturned(Quantity.of(1));
        this.invoiceHibernateRepository.update(expectedInvoice);

        // then
        Invoice actualInvoice = this.invoiceHibernateRepository.findInvoiceById(expectedInvoice.getId()).get();
        Assertions.assertEquals(expectedInvoice, actualInvoice);
    }
}