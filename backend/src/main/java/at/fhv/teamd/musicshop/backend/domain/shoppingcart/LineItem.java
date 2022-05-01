package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO: Think about using dedicated non-persistent ShoppingCart class
@Entity
public class LineItem {
    @Id
    @GeneratedValue
    @Getter private long id;

    @ElementCollection
    private final Map<String, Quantity> quantities = new HashMap<>();

    @Column(nullable = false)
    @Getter private BigDecimal price;

    @OneToOne(cascade = CascadeType.MERGE)
    @Getter private Medium medium;

    protected LineItem() {
    }

    public LineItem(Quantity quantity, Medium medium) {
        this.quantities.put("quantity", quantity);
        this.quantities.put("quantityReturn", Quantity.of(0));
        this.medium = Objects.requireNonNull(medium);
        this.price = medium.getPrice();
    }

    public void increaseQuantity(Quantity quantity) {
        this.quantities.put("quantity", this.quantities.get("quantity").increaseBy(quantity));
    }

    public void decreaseQuantity(Quantity quantity) {
        this.quantities.put("quantity", this.quantities.get("quantity").decreaseBy(quantity));
    }

    public void increaseQuantityReturned(Quantity quantity) {
        if (quantity.getValue() < 1) {
            throw new IllegalArgumentException("ReturnQuantity must be greater than zero");
        }
        if (this.quantities.get("quantityReturn").getValue() + quantity.getValue() > this.quantities.get("quantity").getValue()) {
            throw new IllegalArgumentException("ReturnQuantity to big");
        }
        this.quantities.put("quantityReturn", this.quantities.get("quantityReturn").increaseBy(quantity));
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(this.quantities.get("quantity").getValue()));
    }

    public Quantity getQuantity() {
        return this.quantities.get("quantity");
    }

    public Quantity getQuantityReturn() {
        return this.quantities.get("quantityReturn");
    }
}
