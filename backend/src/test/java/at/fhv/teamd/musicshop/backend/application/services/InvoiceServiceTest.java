package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.InvoiceDTO;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MediumRepository mediumRepository;
    @Mock
    private InvoiceRepository invoiceRepository;

    private InvoiceService invoiceService;

    @BeforeEach
    public void init() {
        RepositoryFactory.setArticleRepository(this.articleRepository);
        RepositoryFactory.setMediumRepository(this.mediumRepository);
        RepositoryFactory.setInvoiceRepository(this.invoiceRepository);
        this.invoiceService = new InvoiceService();
    }

    @Test
    void given_InvoiceService_when_createInvoice_then_repository_contains_invoiceWithoutCustomer() {
        // given
        Invoice expectedInvoice = Invoice.of(Set.of(DomainFactory.createLineItem()));

        // when
        invoiceService.createInvoice(expectedInvoice.getLineItems(), 0);

        // then
        Mockito.verify(invoiceRepository).addInvoice(expectedInvoice);
    }

    @Test
    void given_InvoiceService_when_createInvoice_then_repository_contains_invoiceWithCustomer() {
        // given
        Invoice expectedInvoice = Invoice.of(Set.of(DomainFactory.createLineItem()), 2);

        // when
        invoiceService.createInvoice(expectedInvoice.getLineItems(), 2);

        // then
        Mockito.verify(invoiceRepository).addInvoice(expectedInvoice);
    }

    @Test
    void given_InvoiceService_when_searchInvoiceById_then_return_InvoiceDTO() throws InvoiceException {
        // given
        Invoice invoice = DomainFactory.createInvoice();
        Mockito.when(invoiceRepository.findInvoiceById(invoice.getId())).thenReturn(Optional.of(invoice));
        InvoiceDTO expectedInvoiceDTO = DTOProvider.buildInvoiceDTO(invoice);

        // when
        InvoiceDTO actualInvoiceDTO = invoiceService.searchInvoiceById(invoice.getId());

        // then
        Assertions.assertEquals(expectedInvoiceDTO, actualInvoiceDTO);
    }

    @Test
    void given_InvoiceService_when_returnItem_then_returnQuantity_increased() throws InvoiceException {
        // given
        Invoice invoice = DomainFactory.createInvoice();
        LineItem lineItem = invoice.getLineItems().iterator().next();

        int amountToReturn = 1;

        Mockito.when(invoiceRepository.findInvoiceByLineItemId(lineItem.getId())).thenReturn(Optional.of(invoice));

        // when
        invoiceService.returnItem(DTOProvider.buildLineItemDTO(lineItem), amountToReturn);

        // then
        Assertions.assertEquals(amountToReturn, lineItem.getQuantityReturn().getValue());
        Mockito.verify(this.invoiceRepository).update(invoice);
    }

    @Test
    void given_InvoiceService_when_returnItem_and_quantity_isZero_or_toBig_then_throws() {
        // given
        Invoice invoice = DomainFactory.createInvoice();
        LineItem lineItem = invoice.getLineItems().iterator().next();

        Mockito.when(invoiceRepository.findInvoiceByLineItemId(lineItem.getId())).thenReturn(Optional.of(invoice));

        // when .. then
        int amountToReturn1 = 0;
        Assertions.assertThrows(InvoiceException.class, () -> invoiceService.returnItem(DTOProvider.buildLineItemDTO(lineItem), amountToReturn1));

        int amountToReturn2 = 100;
        Assertions.assertThrows(InvoiceException.class, () -> invoiceService.returnItem(DTOProvider.buildLineItemDTO(lineItem), amountToReturn2));
    }

    @Test
    void given_InvoiceService_when_searchInvoiceById_then_throws_and_doestNotThrow() {
        // given
        Invoice invoice = DomainFactory.createInvoice();
        Mockito.when(invoiceRepository.findInvoiceById(invoice.getId())).thenReturn(Optional.of(invoice));

        // when .. then
        Assertions.assertDoesNotThrow(() -> invoiceService.searchInvoiceById(invoice.getId()));
        Assertions.assertThrows(InvoiceException.class, () -> invoiceService.searchInvoiceById(0L));
    }

    @Test
    void given_InvoiceService_when_returnItem_then_throws_and_doestNotThrow() {
        // given
        Invoice invoice = DomainFactory.createInvoice();
        LineItem lineItem = invoice.getLineItems().iterator().next();

        int amountToReturn = 1;

        Mockito.when(invoiceRepository.findInvoiceByLineItemId(lineItem.getId())).thenReturn(Optional.of(invoice));

        // when .. then
        Assertions.assertDoesNotThrow(() -> invoiceService.returnItem(DTOProvider.buildLineItemDTO(lineItem), amountToReturn));
        Assertions.assertThrows(InvoiceException.class, () -> invoiceService.returnItem(DTOProvider.buildLineItemDTO(DomainFactory.createLineItem()), amountToReturn));
    }
}