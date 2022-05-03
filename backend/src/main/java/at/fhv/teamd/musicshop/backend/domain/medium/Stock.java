package at.fhv.teamd.musicshop.backend.domain.medium;

import at.fhv.teamd.musicshop.backend.domain.Quantity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Access(AccessType.FIELD)
@Embeddable
public class Stock {
    @Embedded
    private Quantity quantity;

    protected Stock() {}

    private Stock(Quantity quantity) {
        this.quantity = quantity;
    }

    public static Stock of(Quantity quantity) {
        return new Stock(quantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(quantity, stock.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
