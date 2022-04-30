package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;
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
    public void given_InvoiceService_when_createInvoice_then_repository_contains_invoiceWithoutCustomer() {
        // given
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 2;

        LineItem lineItem = new LineItem(Quantity.of(amount), medium);
        Invoice expectedInvoice = Invoice.of(Set.of(lineItem));

        // when
        invoiceService.createInvoice(Set.of(lineItem), 0);

        // then
        Mockito.verify(invoiceRepository).addInvoice(expectedInvoice);
    }

    @Test
    public void given_InvoiceService_when_createInvoice_then_repository_contains_invoiceWithCustomer() {
        // given
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 2;

        LineItem lineItem = new LineItem(Quantity.of(amount), medium);
        Invoice expectedInvoice = Invoice.of(Set.of(lineItem), 1);

        // when
        invoiceService.createInvoice(Set.of(lineItem), 1);

        // then
        Mockito.verify(invoiceRepository).addInvoice(expectedInvoice);
    }

    @Test
    public void given_InvoiceService_when_searchInvoiceById_then_return_InvoiceDTO() {
        // given
        Medium medium = DomainFactory.createMedium(MediumType.CD);

        Mockito.when(this.articleRepository.findArticleById(medium.getAlbum().getId())).thenReturn(Optional.of(medium.getAlbum()));
        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        Invoice invoice = DomainFactory.createInvoice();

        Mockito.when(invoiceRepository.findInvoiceById(invoice.getId())).thenReturn(Optional.of(invoice));

        InvoiceDTO expectedInvoiceDTO = DTOProvider.buildInvoiceDTO(articleRepository, invoice);

        // when
        InvoiceDTO actualInvoiceDTO = invoiceService.searchInvoiceById(invoice.getId());

        // then
        Assertions.assertEquals(expectedInvoiceDTO, actualInvoiceDTO);
    }

    @Test
    public void given_InvoiceService_when_returnItem_then_return_quantity_increased() {

    }
}