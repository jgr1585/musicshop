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

        BaseRepositoryData.init();
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
        Assertions.assertEquals(invoice, this.invoiceHibernateRepository.findInvoiceById(invoice.getId()).orElse(null));
        Assertions.assertEquals(Optional.empty(), this.invoiceHibernateRepository.findInvoiceById(0L));
    }

    @Test
    void given_invoiceRepository_when_findInvoiceByLineItemId_returnInvoiceOrEmpty() {
        // given
        Invoice invoice = BaseRepositoryData.getInvoices().stream().iterator().next();

        // when .. then
        LineItem lineItem = invoice.getLineItems().iterator().next();
        Assertions.assertEquals(invoice, this.invoiceHibernateRepository.findInvoiceByLineItemId(lineItem.getId()).orElse(null));
        Assertions.assertEquals(Optional.empty(), this.invoiceHibernateRepository.findInvoiceByLineItemId(0L));
    }

    @Test
    void given_invoiceRepository_when_update_then_updateInvoice() {
        // given
        Invoice expectedInvoice = BaseRepositoryData.getInvoices().iterator().next();
        LineItem selectedLineItem = expectedInvoice.getLineItems().iterator().next();
        Quantity originalQuantity = selectedLineItem.getQuantity();
        int expectedReturnQuantity = 1;

        // when
        selectedLineItem.increaseQuantityReturned(Quantity.of(expectedReturnQuantity));
        this.invoiceHibernateRepository.update(expectedInvoice);

        // then
        Invoice actualInvoice = this.invoiceHibernateRepository.findInvoiceById(expectedInvoice.getId()).orElse(null);
        assert actualInvoice != null;
        LineItem actualLineItem = actualInvoice.getLineItems().stream().filter(selectedLineItem::equals).findFirst().orElse(null);
        assert actualLineItem != null;
        Quantity actualQuantity = actualLineItem.getQuantity();
        Quantity actualReturnQuantity = actualLineItem.getQuantityReturn();

        Assertions.assertEquals(originalQuantity, actualQuantity);
        Assertions.assertEquals(Quantity.of(expectedReturnQuantity), actualReturnQuantity);
    }

    @Test
    void given_invoiceRepository_when_forget_update_then_notUpdateInvoice() {
        // given
        Invoice expectedInvoice = BaseRepositoryData.getInvoices().iterator().next();
        LineItem selectedLineItem = expectedInvoice.getLineItems().iterator().next();
        Quantity originalQuantity = selectedLineItem.getQuantity();
        int expectedReturnQuantity = 1;

        // when
        selectedLineItem.increaseQuantityReturned(Quantity.of(expectedReturnQuantity));
        //Not included this.invoiceHibernateRepository.update(expectedInvoice);

        // then
        Invoice actualInvoice = this.invoiceHibernateRepository.findInvoiceById(expectedInvoice.getId()).orElse(null);
        assert actualInvoice != null;
        LineItem actualLineItem = actualInvoice.getLineItems().stream().filter(selectedLineItem::equals).findFirst().orElse(null);
        assert actualLineItem != null;
        Quantity actualQuantity = actualLineItem.getQuantity();
        Quantity actualReturnQuantity = actualLineItem.getQuantityReturn();

        Assertions.assertEquals(originalQuantity, actualQuantity);
        Assertions.assertNotEquals(Quantity.of(expectedReturnQuantity), actualReturnQuantity);
    }
}