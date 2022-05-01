package at.fhv.teamd.musicshop.backend.domain.medium;

import at.fhv.teamd.musicshop.backend.domain.article.Album;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Medium {
    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private MediumType type;

    @Column
    private BigDecimal price;

    @Embedded
    private Stock stock;

    @ManyToOne(cascade = CascadeType.ALL)
    private Supplier supplier;

    @ManyToOne(cascade = CascadeType.ALL)
    private Album album;

    protected Medium() {
    }

    public Medium(BigDecimal price, MediumType type, Stock stock, Supplier supplier, Album album) {
        this.type = Objects.requireNonNull(type);
        this.price = Objects.requireNonNull(price);
        this.stock = Objects.requireNonNull(stock);
        this.supplier = Objects.requireNonNull(supplier);
        this.album = album;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    //TODO: Look over it again
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medium medium = (Medium) o;
        return id == medium.id && type == medium.type;
    }

    //TODO: FIX
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, type);
//    }
}
