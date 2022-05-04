package at.fhv.teamd.musicshop.backend.domain.invoice;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

class InvoiceTest {

    @Test
    void given_lineItemsSet_when_calculateTotalPrice_then_returnTotalPrice(){
        //given
        Set<LineItem> lineItems = Set.of(
                DomainFactory.createLineItem(),
                DomainFactory.createLineItem(),
                DomainFactory.createLineItem());

        BigDecimal expectedTotalPrice = lineItems.stream()
                .map(LineItem::getTotalPrice)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        //
        Invoice invoice = Invoice.of(lineItems);
        BigDecimal actualTotalPrice = invoice.getTotalPrice();

        //then
        Assertions.assertEquals(expectedTotalPrice, actualTotalPrice);
    }

}