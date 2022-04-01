package at.fhv.teamd.musicshop.backend.domain.medium;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class AnalogMedium extends Medium {
    @Enumerated(EnumType.STRING)
    private AnalogMediumType type;
    @Embedded
    private Stock stock;

    private AnalogMedium() {}

    public AnalogMedium(BigDecimal price, Supplier supplier, AnalogMediumType type, Stock stock) {
        super(price, supplier);

        this.type = Objects.requireNonNull(type);
        this.stock = Objects.requireNonNull(stock);
    }

    public AnalogMediumType getType() {
        return type;
    }

    public Stock getStock() {
        return stock;
    }
}
