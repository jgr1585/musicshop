package at.fhv.teamd.musicshop.backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void given_quantity_when_lessthen0_then_throwsillegalargumentexception() {
        //given
        int value = -6;

        //when...then
        Assertions.assertThrows(IllegalArgumentException.class,() -> Quantity.of(value));
    }

    @Test
    void given_quantity_when_increment_then_incrementquantity() {
        //given
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(21);

        //when
        Assertions.assertEquals(quantityExpected.getValue(), quantity.increment().getValue());
    }

    @Test
    void given_quantity_when_decrement_then_decrementquantity() {
        //given
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(19);

        //when
        Assertions.assertEquals(quantityExpected.getValue(), quantity.decrement().getValue());
    }

}