package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.article.Article;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class LineItem {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String descriptorName;
    @Embedded
    private Quantity quantity;
    @Column
    private BigDecimal price;

    @OneToOne(cascade=CascadeType.ALL)
    private Medium medium;

    protected LineItem() {}

    public LineItem(String descriptorName, Quantity quantity, Medium medium) {
        this.descriptorName = Objects.requireNonNull(descriptorName);
        this.quantity = Objects.requireNonNull(quantity);
        this.medium = Objects.requireNonNull(medium);
        this.price = medium.getPrice();
    }

    public long getId() {
        return id;
    }

    public String getDescriptorName() { return descriptorName; }

    public Quantity getQuantity() {
        return quantity;
    }

    public void increaseQuantity(Quantity quantity) {
        this.quantity = this.quantity.increaseBy(quantity);
    }

    public void decreaseQuantity(Quantity quantity) {
        this.quantity = this.quantity.decreaseBy(quantity);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalPrice() { return price.multiply(BigDecimal.valueOf(quantity.getValue())); }

    public Long getMediumId() {
        return medium.getId();
    }
}
