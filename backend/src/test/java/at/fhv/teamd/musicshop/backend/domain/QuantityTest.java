package at.fhv.teamd.musicshop.backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void given_quantity_when_less_then_0_then_throws_illegalArgumentException() {
        //given
        int value = -6;
        String messageExpected = "Quantity value must be unsigned.";

        //when
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,() -> Quantity.of(value));
        String messageActual = thrown.getMessage();

        //then
        Assertions.assertEquals(messageExpected, messageActual);
    }

    @Test
    void given_quantity_when_increment_then_return_incrementedQuantity() {
        //given
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(21);

        //when
        Quantity quantityActual = quantity.increment();

        //then
        Assertions.assertEquals(quantityExpected, quantityActual);
    }

    @Test
    void given_quantity_when_decrement_then_return_decrementedQuantity() {
        //given
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(19);

        //when
        Quantity quantityActual = quantity.decrement();

        //then
        Assertions.assertEquals(quantityExpected, quantityActual);
    }

    @Test
    void given_quantity_when_increaseBy_then_return_increasedByQuantity() {
        //given
        Quantity quantity = Quantity.of(10);
        Quantity increaseBy = Quantity.of(5);
        Quantity quantityExpected = Quantity.of(quantity.getValue() + increaseBy.getValue());

        //when
        Quantity quantityActual = quantity.increaseBy(increaseBy);

        //then
        Assertions.assertEquals(quantityExpected, quantityActual);
    }

    @Test
    void given_quantity_when_decreaseBy_then_return_decreasedByQuantity() {
        //given
        Quantity quantity = Quantity.of(10);
        Quantity decreaseBy = Quantity.of(5);
        Quantity quantityExpected = Quantity.of(quantity.getValue() - decreaseBy.getValue());

        //when
        Quantity quantityActual = quantity.decreaseBy(decreaseBy);

        //then
        Assertions.assertEquals(quantityExpected, quantityActual);
    }

}