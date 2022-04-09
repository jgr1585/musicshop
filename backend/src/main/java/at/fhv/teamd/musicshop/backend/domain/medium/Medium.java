package at.fhv.teamd.musicshop.backend.domain.medium;

import at.fhv.teamd.musicshop.backend.domain.article.Article;
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
    private Article article;

    protected Medium() {
    }

    public Medium(BigDecimal price, MediumType type, Stock stock, Supplier supplier, Article article) {
        this.type = Objects.requireNonNull(type);
        this.price = Objects.requireNonNull(price);
        this.stock = Objects.requireNonNull(stock);
        this.supplier = Objects.requireNonNull(supplier);
        this.article = article;
    }
}
