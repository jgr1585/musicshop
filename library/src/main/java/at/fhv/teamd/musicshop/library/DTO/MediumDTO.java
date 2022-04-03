package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public final class MediumDTO implements Serializable {
    private static final long serialVersionUID = -6811236899891117944L;

    private Long id;

    private BigDecimal price;
    private String type;
    private SupplierDTO supplier;
    private Integer stockQuantity;
    private Set<Long> articleIDs;

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

    public Set<Long> articleIDs() {
        return this.articleIDs;
    }

    private MediumDTO() {
    }

    public static class Builder {
        private MediumDTO instance;

        private Builder() {
            this.instance = new MediumDTO();
        }

        public MediumDTO.Builder withMediumData(
                Long id,
                BigDecimal price,
                SupplierDTO supplier,
                String type,
                Integer stockQuantity,
                Set<Long> articleIDs
        ) {
            this.instance.id = id;
            this.instance.price = price;
            this.instance.supplier = supplier;
            this.instance.type = type;
            this.instance.stockQuantity = stockQuantity;
            this.instance.articleIDs = articleIDs;
            return this;
        }

        public MediumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in MediumDTO");
            return this.instance;
        }
    }
}
