package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class LineItemTest {
    @Test
    void given_quantity_when_increaseQuantity_then_returnIncreasedQuantity() {
        //given
        LineItem lineItem = new LineItem(Quantity.of(30), DomainFactory.createMedium(MediumType.CD));
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(lineItem.getQuantity().getValue() + quantity.getValue());

        //when
        lineItem.increaseQuantity(quantity);
        Quantity quantityActual = lineItem.getQuantity();

        //then
        Assertions.assertEquals(quantityExpected.getValue(), quantityActual.getValue());
    }

    @Test
    void given_quantity_when_decreaseQuantity_then_returnDecreasedQuantity() {
        //given
        LineItem lineItem = new LineItem(Quantity.of(30), DomainFactory.createMedium(MediumType.CD));
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(lineItem.getQuantity().getValue() - quantity.getValue());

        //when
        lineItem.decreaseQuantity(quantity);
        Quantity quantityActual = lineItem.getQuantity();

        //then
        Assertions.assertEquals(quantityExpected.getValue(), quantityActual.getValue());
    }

    @Test
    void given_lineItemDetails_when_getDetails_then_detailsEqual() {
        //given
        Quantity quantity = Quantity.of(60);
        Medium medium = DomainFactory.createMedium(MediumType.VINYL);
        BigDecimal price = medium.getPrice();
        long mediumId = medium.getId();
        LineItem lineItem = new LineItem(quantity, medium);

        //when
        Quantity quantityActual = lineItem.getQuantity();
        BigDecimal priceActual = lineItem.getPrice();
        long mediumIdActual = lineItem.getMedium().getId();

        //then
        Assertions.assertEquals(quantity.getValue(), quantityActual.getValue());
        Assertions.assertEquals(price, priceActual);
        Assertions.assertEquals(mediumId, mediumIdActual);
    }

    @Test
    void given_negativeQuantity_when_increaseQuantityReturned_then_throwsIllegalArgumentException() {
        //given
        LineItem lineItem = new LineItem(Quantity.of(30), DomainFactory.createMedium(MediumType.CD));
        int value = 0;
        String messageExpected = "Quantity to return must be greater than zero";

        //when
        final Quantity quantity = Quantity.of(value);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,() -> lineItem.increaseQuantityReturned(quantity));
        String messageActual = thrown.getMessage();

        //then
        Assertions.assertEquals(messageExpected, messageActual);
    }

    @Test
    void given_toBigQuantity_when_increaseQuantityReturned_then_throwsIllegalArgumentException() {
        //given
        LineItem lineItem = new LineItem(Quantity.of(30), DomainFactory.createMedium(MediumType.CD));
        int value = 31;
        String messageExpected = "Quantity to return is to big";

        //when
        final Quantity quantity = Quantity.of(value);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,() -> lineItem.increaseQuantityReturned(quantity));
        String messageActual = thrown.getMessage();

        //then
        Assertions.assertEquals(messageExpected, messageActual);
    }

    @Test
    void given_priceAndQuantity_when_getTotalPrice_then_returnTotalPrice() {
        //given
        Medium medium = DomainFactory.createMedium(MediumType.DIGITAL);
        Quantity quantity = Quantity.of(20);
        BigDecimal totalPrice = medium.getPrice().multiply(BigDecimal.valueOf(quantity.getValue()));
        LineItem lineItem = new LineItem(quantity, medium);

        //when
        BigDecimal totalPriceActual = lineItem.getTotalPrice();

        //then
        Assertions.assertEquals(totalPrice, totalPriceActual);
    }

}
