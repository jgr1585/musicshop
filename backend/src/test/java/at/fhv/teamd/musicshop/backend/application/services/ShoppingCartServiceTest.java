package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.LineItemDTO;
import at.fhv.teamd.musicshop.library.dto.MediumDTO;
import at.fhv.teamd.musicshop.library.dto.ShoppingCartDTO;
import at.fhv.teamd.musicshop.library.exceptions.ShoppingCartException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    private final MediumRepository mediumRepository;

    private final ShoppingCartService shoppingCartService;

    private ShoppingCartServiceTest() {
        this.mediumRepository = RepositoryFactory.getMediumRepositoryInstance();
        this.shoppingCartService = new ShoppingCartService();
    }

    @Test
    void given_shoppingCartService_when_addToShoppingCart_then_returnRefreshedShoppingCart() throws ShoppingCartException {
        //given
        String userId = "user123456";
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 2;

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        LineItem lineItem = new LineItem(Quantity.of(amount), medium);
        Set<LineItemDTO> expectedLineItems = Set.of(DTOProvider.buildLineItemDTO(lineItem));

        LineItem lineItemIncreasedAmount = new LineItem(Quantity.of(amount * 2), medium);
        Set<LineItemDTO> expectedLineItemsIncreasedAmount = Set.of(DTOProvider.buildLineItemDTO(lineItemIncreasedAmount));

        //when
        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId, mediumDTO.id(), amount);
        ShoppingCartDTO shoppingCartDTO = this.shoppingCartService.getPhysicalMediumShoppingCart(userId);
        Set<LineItemDTO> actualLineItems = shoppingCartDTO.lineItems();

        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId, mediumDTO.id(), amount);
        ShoppingCartDTO shoppingCartDTOIncreasedAmount = this.shoppingCartService.getPhysicalMediumShoppingCart(userId);
        Set<LineItemDTO> actualLineItemsIncreasedAmount = shoppingCartDTOIncreasedAmount.lineItems();

        //then
        Assertions.assertTrue(expectedLineItems.containsAll(actualLineItems) && actualLineItems.containsAll(expectedLineItems));
        Assertions.assertTrue(expectedLineItemsIncreasedAmount.containsAll(actualLineItemsIncreasedAmount) && actualLineItemsIncreasedAmount.containsAll(expectedLineItemsIncreasedAmount));
    }

    @Test
    void given_shoppingCartService_when_removeFromShoppingCart_then_returnRefreshedShoppingCart() throws ShoppingCartException {
        //given
        String userId = "user12345";
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 3;

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId, mediumDTO.id(), amount);

        //when remove 1 of 3
        this.shoppingCartService.removePhysicalMediumFromShoppingCart(userId, mediumDTO.id(), 1);

        //then quantity should equal 2
        int expectedAmount = 2;
        Assertions.assertEquals(expectedAmount, this.shoppingCartService.getPhysicalMediumShoppingCart(userId).lineItems().stream().findFirst().orElseThrow().quantity().intValue());

        //when remove 2 of 2
        this.shoppingCartService.removePhysicalMediumFromShoppingCart(userId, mediumDTO.id(), 2);

        //then quantity should equal 0
        Assertions.assertTrue(this.shoppingCartService.getPhysicalMediumShoppingCart(userId).lineItems().isEmpty());
        Assertions.assertThrows(ShoppingCartException.class, () -> this.shoppingCartService.removePhysicalMediumFromShoppingCart("user0000", mediumDTO.id(), 10));
    }

    @Test
    void given_articlesInShoppingCart_when_buyFromShoppingCart_then_returnEmptyShoppingCart() throws ShoppingCartException {
        //given
        String userId = "user1234567";
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 1;
        int expectedAmount = medium.getStock().getQuantity().getValue() - amount;

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);

        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId, mediumDTO.id(), amount);

        //when
        this.shoppingCartService.buyFromPhysicalMediumShoppingCart(userId, 0);

        //then
        Assertions.assertTrue(this.shoppingCartService.getPhysicalMediumShoppingCart(userId).lineItems().isEmpty());
        Assertions.assertEquals(expectedAmount, medium.getStock().getQuantity().getValue());
    }

    @Test
    void given_emptyShoppingCart_when_buyFromShoppingCart_then_returnEmptyShoppingCart() {
        //given
        String userId = "user1239";

        //when
        Assertions.assertThrows(ShoppingCartException.class, () -> this.shoppingCartService.buyFromPhysicalMediumShoppingCart(userId, 0));

        //then
        Assertions.assertTrue(this.shoppingCartService.getPhysicalMediumShoppingCart(userId).lineItems().isEmpty());
    }

    @Test
    void given_articlesInShoppingCart_when_buyFromShoppingCart_item_not_in_stock_then_Throw_Exeption() throws ShoppingCartException {
        //given
        String userId1 = "user1234";
        String userId2 = "user5678";
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        int amount = 3;

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);
        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId1, mediumDTO.id(), amount);
        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId2, mediumDTO.id(), amount);

        //when
        Assertions.assertDoesNotThrow(() -> this.shoppingCartService.buyFromPhysicalMediumShoppingCart(userId1, 0));
        Assertions.assertThrows(ShoppingCartException.class,() -> this.shoppingCartService.buyFromPhysicalMediumShoppingCart(userId2, 0));

        //then
        Assertions.assertTrue(this.shoppingCartService.getPhysicalMediumShoppingCart(userId1).lineItems().isEmpty());
        Assertions.assertFalse(this.shoppingCartService.getPhysicalMediumShoppingCart(userId2).lineItems().isEmpty());
    }

    @Test
    void given_shoppingCart_when_initializeShoppingCart_then_returnEqual() throws ShoppingCartException {
        //given
        String userId1 = "user1236";
        String userId2 = "user5679";
        Medium medium = DomainFactory.createMedium(MediumType.CD);

        Mockito.when(this.mediumRepository.findMediumById(medium.getId())).thenReturn(Optional.of(medium));

        MediumDTO mediumDTO = DTOProvider.buildMediumDTO(medium);
        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId1, mediumDTO.id(), 2);
        this.shoppingCartService.addPhysicalMediumToShoppingCart(userId2, mediumDTO.id(), 4);

        //when
        ShoppingCartDTO shoppingCartDTO1 = this.shoppingCartService.getPhysicalMediumShoppingCart(userId1);
        ShoppingCartDTO shoppingCartDTO2 = this.shoppingCartService.getPhysicalMediumShoppingCart(userId1);
        ShoppingCartDTO shoppingCartDTO3 = this.shoppingCartService.getPhysicalMediumShoppingCart(userId2);

        //then
        Assertions.assertEquals(shoppingCartDTO1, shoppingCartDTO2);
        Assertions.assertNotEquals(shoppingCartDTO1, shoppingCartDTO3);
    }
}