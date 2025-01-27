package at.fhv.teamd.musicshop.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class MediumDTO implements Serializable {
    private static final long serialVersionUID = -6811236899891117944L;

    @JsonProperty(required = true)
    private Long id;
    @JsonProperty(required = true)
    private BigDecimal price;
    @JsonProperty(required = true)
    private String type;
    @JsonProperty(required = true)
    private SupplierDTO supplier;
    @JsonProperty(required = true)
    private Integer stockQuantity;

    public static MediumDTO.Builder builder() {
        return new MediumDTO.Builder();
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

    private MediumDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediumDTO mediumDTO = (MediumDTO) o;
        return Objects.equals(id, mediumDTO.id) && Objects.equals(price, mediumDTO.price) && Objects.equals(type, mediumDTO.type) && Objects.equals(supplier, mediumDTO.supplier) && Objects.equals(stockQuantity, mediumDTO.stockQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, type, supplier, stockQuantity);
    }

    public static class Builder {
        private final MediumDTO instance;

        private Builder() {
            this.instance = new MediumDTO();
        }

        public MediumDTO.Builder withMediumData(
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

        public MediumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in MediumDTO");
            return this.instance;
        }
    }
}
