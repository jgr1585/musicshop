package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.library.DTO.MediumDTO;
import at.fhv.teamd.musicshop.library.DTO.ShoppingCartDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
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

    private ShoppingCartService shoppingCartService;

    @Mock
    private MediumRepository mediumRepository;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void init() {
        RepositoryFactory.setMediumRepository(this.mediumRepository);
        RepositoryFactory.setArticleRepository(this.articleRepository);

        this.shoppingCartService = new ShoppingCartService();
    }


    @Test
    public void given_shoppingCartService_when_addToShoppingCart_then_returnRefreshedShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        Article article = DomainFactory.createArticle();
        Medium medium = article.getMediums().stream().findFirst().get();
        int amount = 2;

        Mockito.when(this.articleRepository.findArticleById(article.getId())).thenReturn(Optional.of(article));
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        ArticleDTO articleDTO = DTOProvider.buildArticleDTO(this.mediumRepository, article);
        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        LineItem lineItem = new LineItem(article.getDescriptorName(), Quantity.of(amount), medium, article);
        Set<LineItemDTO> expectedLineItems = Set.of(DTOProvider.buildLineItemDTO(this.articleRepository, this.mediumRepository, lineItem));

        LineItem lineItemIncreasedAmount = new LineItem(article.getDescriptorName(), Quantity.of(amount * 2), medium, article);
        Set<LineItemDTO> expectedLineItemsIncreasedAmount = Set.of(DTOProvider.buildLineItemDTO(this.articleRepository, this.mediumRepository, lineItemIncreasedAmount));


        //when
        this.shoppingCartService.addToShoppingCart(uuid, articleDTO, mediumDTO, amount);
        ShoppingCartDTO shoppingCartDTO = this.shoppingCartService.getShoppingCart(uuid);
        Set<LineItemDTO> actualLineItems = shoppingCartDTO.lineItems();

        this.shoppingCartService.addToShoppingCart(uuid, articleDTO, mediumDTO, amount);
        ShoppingCartDTO shoppingCartDTOIncreasedAmount = this.shoppingCartService.getShoppingCart(uuid);
        Set<LineItemDTO> actualLineItemsIncreasedAmount = shoppingCartDTOIncreasedAmount.lineItems();

        //then
        Assertions.assertTrue(expectedLineItems.containsAll(actualLineItems) && actualLineItems.containsAll(expectedLineItems));
        Assertions.assertTrue(expectedLineItemsIncreasedAmount.containsAll(actualLineItemsIncreasedAmount) && actualLineItemsIncreasedAmount.containsAll(expectedLineItemsIncreasedAmount));
    }

    @Test
    public void given_shoppingCart_when_initializeShoppingcart_then_returnShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        AtomicReference<Method> method = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> method.set(ShoppingCartService.class.getDeclaredMethod("initializeShoppingCart", UUID.class)));
        method.get().setAccessible(true);

        //when
        Assertions.assertDoesNotThrow(() -> method.get().invoke(this.shoppingCartService, uuid));

        //then
        Assertions.assertThrows(InvocationTargetException.class, () -> method.get().invoke(this.shoppingCartService, uuid));
    }



    @Test
    public void given_shoppingCartService_when_removeFromShoppingCart_then_returnRefreshedShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();
        Article article = DomainFactory.createArticle();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 3;

        Mockito.when(this.articleRepository.findArticleById(article.getId())).thenReturn(Optional.of(article));
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        ArticleDTO articleDTO = DTOProvider.buildArticleDTO(this.mediumRepository, article);
        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addToShoppingCart(uuid, articleDTO, mediumDTO, amount);

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
        Article article = DomainFactory.createArticle();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 1;

        Mockito.when(this.articleRepository.findArticleById(article.getId())).thenReturn(Optional.of(article));
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        ArticleDTO articleDTO = DTOProvider.buildArticleDTO(this.mediumRepository, article);
        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addToShoppingCart(uuid, articleDTO, mediumDTO, amount);

        //when
        boolean buyFromShoppingCart = this.shoppingCartService.buyFromShoppingCart(uuid, 0);

        //then
        Assertions.assertTrue(buyFromShoppingCart);
        Assertions.assertTrue(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_emptyShoppingCart_when_buyFromShoppingCart_then_returnEmptyShoppingCart() {
        //given
        UUID uuid = UUID.randomUUID();

        //when
        boolean buyFromShoppingCart = this.shoppingCartService.buyFromShoppingCart(uuid, 0);

        //then
        Assertions.assertTrue(buyFromShoppingCart);
        Assertions.assertTrue(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_articlesInShoppingCart_when_buyFromShoppingCart_item_not_in_stock_then_Throw_Exeption() {
        //given
        UUID uuid = UUID.randomUUID();
        Article article = DomainFactory.createArticle();
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 10;

        Mockito.when(this.articleRepository.findArticleById(article.getId())).thenReturn(Optional.of(article));
        Mockito.when(this.mediumRepository.findMediumById(article.getMediumIDs().stream().findFirst().get())).thenReturn(article.getMediums().stream().findFirst());

        ArticleDTO articleDTO = DTOProvider.buildArticleDTO(this.mediumRepository, article);
        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addToShoppingCart(uuid, articleDTO, mediumDTO, amount);

        //when
        Assertions.assertThrows(RuntimeException.class,() -> this.shoppingCartService.buyFromShoppingCart(uuid, 0));

        //then
        Assertions.assertFalse(this.shoppingCartService.getShoppingCart(uuid).lineItems().isEmpty());
    }

    @Test
    public void given_shoppingCart_when_initializeShoppingCart_then_returnEqual(){
        //given
        UUID uuid1 = UUID.randomUUID();
        Article article1 = DomainFactory.createArticle();
        Medium medium1 = article1.getMediums().stream().findFirst().get();
        UUID uuid2 = UUID.randomUUID();
        Article article2 = DomainFactory.createArticle();
        Medium medium2 = article2.getMediums().stream().findFirst().get();

        Mockito.when(this.articleRepository.findArticleById(article1.getId())).thenReturn(Optional.of(article1));
        Mockito.when(this.articleRepository.findArticleById(article2.getId())).thenReturn(Optional.of(article2));
        Mockito.when(this.mediumRepository.findMediumById(article1.getMediumIDs().stream().findFirst().get())).thenReturn(article1.getMediums().stream().findFirst());
        Mockito.when(this.mediumRepository.findMediumById(article2.getMediumIDs().stream().findFirst().get())).thenReturn(article2.getMediums().stream().findFirst());

        ArticleDTO articleDTO1 = DTOProvider.buildArticleDTO(mediumRepository, article1);
        ArticleDTO articleDTO2 = DTOProvider.buildArticleDTO(mediumRepository, article2);
        MediumDTO mediumDTO1 = DTOProvider.buildMediumDTO(medium1);
        MediumDTO mediumDTO2 = DTOProvider.buildMediumDTO(medium2);

        //when
        this.shoppingCartService.getShoppingCart(uuid1);
        this.shoppingCartService.getShoppingCart(uuid2);
        this.shoppingCartService.addToShoppingCart(uuid1, articleDTO1, mediumDTO1, 2);
        this.shoppingCartService.addToShoppingCart(uuid2, articleDTO2, mediumDTO2, 2);
        ShoppingCartDTO shoppingCartDTO1 = this.shoppingCartService.getShoppingCart(uuid1);
        ShoppingCartDTO shoppingCartDTO2 = this.shoppingCartService.getShoppingCart(uuid1);
        ShoppingCartDTO shoppingCartDTO3 = this.shoppingCartService.getShoppingCart(uuid2);
        ShoppingCartDTO shoppingCartDTO4 = this.shoppingCartService.getShoppingCart(uuid2);

        //then
        Assertions.assertEquals(shoppingCartDTO1, shoppingCartDTO2);
        Assertions.assertEquals(shoppingCartDTO3, shoppingCartDTO4);
        Assertions.assertNotEquals(shoppingCartDTO1, shoppingCartDTO3);

    }



}