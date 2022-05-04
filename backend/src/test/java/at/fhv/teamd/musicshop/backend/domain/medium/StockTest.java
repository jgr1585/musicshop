package at.fhv.teamd.musicshop.backend.domain.medium;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void of() {
    }

    @Test
    void given_stock_when_getQuantity_then_returnQuantity() {
        //given
        Quantity quantityExpected = Quantity.of(60);
        Stock stock = Stock.of(quantityExpected);

        //when
        Quantity quantityActual = stock.getQuantity();

        //
        Assertions.assertEquals(quantityExpected, quantityActual);
    }
}