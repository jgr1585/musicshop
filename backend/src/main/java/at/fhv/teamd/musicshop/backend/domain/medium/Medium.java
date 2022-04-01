package at.fhv.teamd.musicshop.backend.domain.medium;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Medium {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private BigDecimal price;

    @OneToOne(cascade= CascadeType.ALL)
    private Supplier supplier;

    protected Medium() {}

    public Medium(BigDecimal price, Supplier supplier) {
        this.price = Objects.requireNonNull(price);
        this.supplier = Objects.requireNonNull(supplier);
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
