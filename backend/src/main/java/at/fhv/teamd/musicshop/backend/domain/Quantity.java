package at.fhv.teamd.musicshop.backend.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Access(AccessType.FIELD)
@Embeddable
public class Quantity {
    private int value;

    protected Quantity() {}

    private Quantity(int value) {
        this.value = value;
    }

    public static Quantity of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity value must be unsigned.");
        }

        return new Quantity(value);
    }

    public int getValue() {
        return value;
    }

    public Quantity increment() {
        return Quantity.of(this.value + 1);
    }

    public Quantity decrement() {
        return Quantity.of(this.value - 1);
    }

    public Quantity increaseBy(Quantity quantity) {
        return Quantity.of(this.value + quantity.value);
    }

    public Quantity decreaseBy(Quantity quantity) {
        return Quantity.of(this.value - quantity.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
