package at.fhv.teamd.musicshop.backend.domain.medium;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MediumTest {

    @Test
    void given_medium_when_setStock_then_updatedStock() {
        //given
        Medium medium = DomainFactory.createMedium(MediumType.CD);
        Stock stockExpected = Stock.of(Quantity.of(20));

        //when
        medium.setStock(Stock.of(Quantity.of(20)));
        Stock stockActual = medium.getStock();

        //then
        Assertions.assertEquals(stockExpected, stockActual);
    }
}