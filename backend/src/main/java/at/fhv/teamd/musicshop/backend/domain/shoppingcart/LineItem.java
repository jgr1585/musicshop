package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Entity
public class LineItem {
    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private Quantity quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.MERGE)
    private Medium medium;

    protected LineItem() {
    }

    public LineItem(Quantity quantity, Medium medium) {
        this.quantity = Objects.requireNonNull(quantity);
        this.medium = Objects.requireNonNull(medium);
        this.price = medium.getPrice();
    }

    public void increaseQuantity(Quantity quantity) {
        this.quantity = this.quantity.increaseBy(quantity);
    }

    public void decreaseQuantity(Quantity quantity) {
        this.quantity = this.quantity.decreaseBy(quantity);
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity.getValue()));
    }
}
