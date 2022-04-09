package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private MediumRepository mediumRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    private ShoppingCartService shoppingCartService;

    @BeforeEach
    public void init() {
        RepositoryFactory.setArticleRepository(this.articleRepository);
        RepositoryFactory.setMediumRepository(this.mediumRepository);
        RepositoryFactory.setEmployeeRepository(this.employeeRepository);
        RepositoryFactory.setInvoiceRepository(this.invoiceRepository);
        this.shoppingCartService = new ShoppingCartService();
    }

    @Test
    public void given_shoppingCartService_when_addToShoppingCart_then_returnRefreshedShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 2;

        Mockito.when(this.articleRepository.findArticleById(medium.getArticle().getId())).thenReturn(Optional.of(medium.getArticle()));
        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        LineItem lineItem = new LineItem(Quantity.of(amount), medium);
        Set<LineItemDTO> expectedLineItems = Set.of(DTOProvider.buildLineItemDTO(this.articleRepository, this.mediumRepository, lineItem));

        LineItem lineItemIncreasedAmount = new LineItem(Quantity.of(amount * 2), medium);
        Set<LineItemDTO> expectedLineItemsIncreasedAmount = Set.of(DTOProvider.buildLineItemDTO(this.articleRepository, this.mediumRepository, lineItemIncreasedAmount));

        //when
        this.shoppingCartService.addToShoppingCart(uuid, mediumDTO, amount);
        ShoppingCartDTO shoppingCartDTO = this.shoppingCartService.getShoppingCart(uuid);
        Set<LineItemDTO> actualLineItems = shoppingCartDTO.lineItems();

        this.shoppingCartService.addToShoppingCart(uuid, mediumDTO, amount);
        ShoppingCartDTO shoppingCartDTOIncreasedAmount = this.shoppingCartService.getShoppingCart(uuid);
        Set<LineItemDTO> actualLineItemsIncreasedAmount = shoppingCartDTOIncreasedAmount.lineItems();

        //then
        Assertions.assertTrue(expectedLineItems.containsAll(actualLineItems) && actualLineItems.containsAll(expectedLineItems));
        Assertions.assertTrue(expectedLineItemsIncreasedAmount.containsAll(actualLineItemsIncreasedAmount) && actualLineItemsIncreasedAmount.containsAll(expectedLineItemsIncreasedAmount));
    }

    // TODO: rebuilt test
//    @Test
//    public void given_shoppingCart_when_initializeShoppingcart_then_returnShoppingCart() {
//        //given
//        UUID uuid = UUID.randomUUID();
//        AtomicReference<Method> method = new AtomicReference<>();
//        Assertions.assertDoesNotThrow(() -> method.set(ShoppingCartService.class.getDeclaredMethod("initializeShoppingCart", UUID.class)));
//        method.get().setAccessible(true);
//
//        //when
//        Assertions.assertDoesNotThrow(() -> method.get().invoke(this.shoppingCartService, uuid));
//
//        //then
//        Assertions.assertThrows(InvocationTargetException.class, () -> method.get().invoke(this.shoppingCartService, uuid));
//    }

    @Test
    public void given_shoppingCartService_when_removeFromShoppingCart_then_returnRefreshedShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 3;

        Mockito.when(this.articleRepository.findArticleById(medium.getArticle().getId())).thenReturn(Optional.of(medium.getArticle()));
        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addToShoppingCart(uuid, mediumDTO, amount);

        //when remove 1 of 3
        this.shoppingCartService.removeFromShoppingCart(uuid, mediumDTO, 1);

        //then quantity should equal 2
        int expectedAmount = 2;
        Assertions.assertEquals(expectedAmount, this.shoppingCartService.getShoppingCart(uuid).lineItems().stream().findFirst().get().quantity().intValue());

        //when remove 2 of 2
        this.shoppingCartService.removeFromShoppingCart(uuid, mediumDTO, 2);

        //then quantity should equal 2
        Assertions.assertTrue(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
        Assertions.assertThrows(NoSuchElementException.class, () -> this.shoppingCartService.removeFromShoppingCart(UUID.randomUUID(), mediumDTO, 1));
    }

    @Test
    public void given_articlesInShoppingCart_when_buyFromShoppingCart_then_returnEmptyShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 1;

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addToShoppingCart(uuid, mediumDTO, amount);

        //when
        this.shoppingCartService.buyFromShoppingCart(uuid, 0);

        //then
        Assertions.assertTrue(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_emptyShoppingCart_when_buyFromShoppingCart_then_returnEmptyShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();

        //when
        this.shoppingCartService.buyFromShoppingCart(uuid, 0);

        //then
        Assertions.assertTrue(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_articlesInShoppingCart_when_buyFromShoppingCart_item_not_in_stock_then_Throw_Exeption() {
        //given
        UUID uuid = UUID.randomUUID();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 10;

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);
        this.shoppingCartService.addToShoppingCart(uuid, mediumDTO, amount);

        //when
        // TODO: fix test
//        Assertions.assertThrows(RuntimeException.class,() -> this.shoppingCartService.buyFromShoppingCart(uuid, 0));

        //then
        Assertions.assertFalse(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_shoppingCart_when_initializeShoppingCart_then_returnEqual(){
        //given
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Medium medium1 = DomainFactory.createMedium(MediumType.CD);
        Medium medium2 = DomainFactory.createMedium(MediumType.CASSETTE);

        Mockito.when(this.articleRepository.findArticleById(medium1.getArticle().getId())).thenReturn(Optional.of(medium1.getArticle()));
        Mockito.when(this.articleRepository.findArticleById(medium2.getArticle().getId())).thenReturn(Optional.of(medium2.getArticle()));
        Mockito.when(this.mediumRepository.findMediumById(medium1.getId())).thenReturn(Optional.of(medium1));
        Mockito.when(this.mediumRepository.findMediumById(medium2.getId())).thenReturn(Optional.of(medium2));

        MediumDTO mediumDTO1 = DTOProvider.buildMediumDTO(medium1);
        MediumDTO mediumDTO2 = DTOProvider.buildMediumDTO(medium2);

        //when
        this.shoppingCartService.addToShoppingCart(uuid1, mediumDTO1, 2);
        this.shoppingCartService.addToShoppingCart(uuid2, mediumDTO2, 2);
        ShoppingCartDTO shoppingCartDTO1 = this.shoppingCartService.getShoppingCart(uuid1);
        ShoppingCartDTO shoppingCartDTO2 = this.shoppingCartService.getShoppingCart(uuid1);
        ShoppingCartDTO shoppingCartDTO3 = this.shoppingCartService.getShoppingCart(uuid2);

        //then
        Assertions.assertEquals(shoppingCartDTO1, shoppingCartDTO2);
        // TOOD: fix test
//        Assertions.assertNotEquals(shoppingCartDTO1, shoppingCartDTO3);
    }
}