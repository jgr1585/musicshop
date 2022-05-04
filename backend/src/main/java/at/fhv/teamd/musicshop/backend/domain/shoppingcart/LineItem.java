package at.fhv.teamd.musicshop.backend.domain.shoppingcart;

import at.fhv.teamd.musicshop.backend.domain.Quantity;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// TODO: Think about using dedicated non-persistent ShoppingCart class
@Entity
public class LineItem {
    @Id
    @GeneratedValue
    @Getter private long id;

    @Column(unique = true)
    @Getter private UUID uuid;

    @ElementCollection
    private final Map<Quantities, Quantity> quantities = new HashMap<>();

    @Column(nullable = false)
    @Getter private BigDecimal price;

    @OneToOne(cascade = CascadeType.MERGE)
    @Getter private Medium medium;

    protected LineItem() {
    }

    public LineItem(Quantity quantity, Medium medium) {
        this.uuid = UUID.randomUUID();
        this.quantities.put(Quantities.QUANTITY, quantity);
        this.quantities.put(Quantities.QUANTITY_RETURN, Quantity.of(0));
        this.medium = Objects.requireNonNull(medium);
        this.price = medium.getPrice();
    }

    public void increaseQuantity(Quantity quantity) {
        this.quantities.put(Quantities.QUANTITY, this.quantities.get(Quantities.QUANTITY).increaseBy(quantity));
    }

    public void decreaseQuantity(Quantity quantity) {
        this.quantities.put(Quantities.QUANTITY, this.quantities.get(Quantities.QUANTITY).decreaseBy(quantity));
    }

    public void increaseQuantityReturned(Quantity quantity) {
        if (quantity.getValue() < 1) {
            throw new IllegalArgumentException("Quantity to return must be greater than zero");
        }
        if (this.quantities.get(Quantities.QUANTITY_RETURN).getValue() + quantity.getValue() > this.quantities.get(Quantities.QUANTITY).getValue()) {
            throw new IllegalArgumentException("Quantity to return is to big");
        }
        this.quantities.put(Quantities.QUANTITY_RETURN, this.quantities.get(Quantities.QUANTITY_RETURN).increaseBy(quantity));
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(this.quantities.get(Quantities.QUANTITY).getValue()));
    }

    public Quantity getQuantity() {
        return this.quantities.get(Quantities.QUANTITY);
    }

    public Quantity getQuantityReturn() {
        return this.quantities.get(Quantities.QUANTITY_RETURN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return uuid.equals(lineItem.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    private enum Quantities {
        QUANTITY, QUANTITY_RETURN
    }
}
