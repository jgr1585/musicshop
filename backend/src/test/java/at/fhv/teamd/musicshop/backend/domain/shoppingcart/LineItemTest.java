package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class LineItemTest {
    @Test
    void given_quantity_when_increasequantity_then_returnincreasedqunatity() {
        //given
        LineItem lineItem = new LineItem("", Quantity.of(30), DomainFactory.createMedium(MediumType.CD), DomainFactory.createArticle());
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(50);

        //when
        lineItem.increaseQuantity(quantity);
        Quantity quantityActual = lineItem.getQuantity();

        //then
        Assertions.assertEquals(quantityExpected.getValue(), quantityActual.getValue());
    }

    @Test
    void given_quantity_when_decreasequantity_then_returndecreasedqunatity() {
        //given
        LineItem lineItem = new LineItem("", Quantity.of(30), DomainFactory.createMedium(MediumType.CD), DomainFactory.createArticle());
        Quantity quantity = Quantity.of(20);
        Quantity quantityExpected = Quantity.of(10);

        //when
        lineItem.decreaseQuantity(quantity);
        Quantity quantityActual = lineItem.getQuantity();

        //then
        Assertions.assertEquals(quantityExpected.getValue(), quantityActual.getValue());
    }

    @Test
    void given_lineItemdetails_whengetdetails_then_detailsequal() {
        //given
        String descriptorName = "Article descriptor";
        Quantity quantity = Quantity.of(60);
        Medium medium = DomainFactory.createMedium(MediumType.VINYL);
        Article article = DomainFactory.createArticle();
        BigDecimal price = medium.getPrice();
        long articleId = article.getId();
        long mediumId = medium.getId();
        LineItem lineItem = new LineItem(descriptorName, quantity, medium, article);

        //when
        String descriptorNameActual = lineItem.getDescriptorName();
        Quantity quantityActual = lineItem.getQuantity();
        BigDecimal priceActual = lineItem.getPrice();
        long articleIdActual = lineItem.getArticleId();
        long mediumIdActual = lineItem.getMediumId();

        //then
        Assertions.assertEquals(descriptorName, descriptorNameActual);
        Assertions.assertEquals(quantity.getValue(), quantityActual.getValue());
        Assertions.assertEquals(price, priceActual);
        Assertions.assertEquals(articleId, articleIdActual);
        Assertions.assertEquals(mediumId, mediumIdActual);
    }

    @Test
    void given_priceandquantity_when_gettotalprice_then_returntotalprice() {
        //given
        Medium medium = DomainFactory.createMedium(MediumType.DIGITAL);
        Quantity quantity = Quantity.of(20);
        BigDecimal totalPrice = BigDecimal.valueOf(20 * medium.getPrice().intValue());
        LineItem lineItem = new LineItem("", quantity, medium, DomainFactory.createArticle());

        //when
        BigDecimal totalPriceActual = lineItem.getTotalPrice();

        //then
        Assertions.assertEquals(totalPrice, totalPriceActual);
    }

}
