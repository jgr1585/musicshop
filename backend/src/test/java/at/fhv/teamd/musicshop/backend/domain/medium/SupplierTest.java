package at.fhv.teamd.musicshop.backend.domain.medium;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class SupplierTest {

    @Test
    void given_supplierDetails_when_getDetails_returnDetails() {
        //given
        String nameExpected = "Test Supplier";
        Duration durationExpected = Duration.ofDays(6);
        Supplier supplier = new Supplier(nameExpected, durationExpected);

        //when
        String nameActual = supplier.getName();
        Duration durationActual = supplier.getSupplyDuration();

        //then
        Assertions.assertEquals(nameExpected, nameActual);
        Assertions.assertEquals(durationExpected, durationActual);
    }
}