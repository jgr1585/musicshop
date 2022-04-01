package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class AnalogMediumDTO implements MediumDTO, Serializable {
    private static final long serialVersionUID = -6811236899891117944L;

    private Long id;

    private BigDecimal price;
    private SupplierDTO supplier;
    private String type;
    private Integer stockQuantity;

    public static AnalogMediumDTO.Builder builder() {
        return new AnalogMediumDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public BigDecimal price() {
        return this.price;
    }

    public SupplierDTO supplier() {
        return this.supplier;
    }

    public String type() {
        return this.type;
    }

    public Integer stockQuantity() {
        return this.stockQuantity;
    }

    private AnalogMediumDTO() {
    }

    public static class Builder {
        private AnalogMediumDTO instance;

        private Builder() {
            this.instance = new AnalogMediumDTO();
        }

        public AnalogMediumDTO.Builder withAnalogMediumData(
                Long id,
                BigDecimal price,
                SupplierDTO supplier,
                String type,
                Integer stockQuantity
        ) {
            this.instance.id = id;
            this.instance.price = price;
            this.instance.supplier = supplier;
            this.instance.type = type;
            this.instance.stockQuantity = stockQuantity;
            return this;
        }

        public AnalogMediumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in AnalogMediumDTO");
            return this.instance;
        }
    }
}
