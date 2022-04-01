package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public final class SupplierDTO implements Serializable {
    private static final long serialVersionUID = 7220444567998280771L;

    private Long id;

    private String name;
    private Duration supplyDuration;

    public static SupplierDTO.Builder builder() {
        return new SupplierDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public Duration supplyDuration() {
        return supplyDuration;
    }

    private SupplierDTO() {
    }

    public static class Builder {
        private SupplierDTO instance;

        private Builder() {
            this.instance = new SupplierDTO();
        }

        public SupplierDTO.Builder withSupplierData(
                Long id,
                String name,
                Duration supplyDuration
        ) {
            this.instance.id = id;
            this.instance.name = name;
            this.instance.supplyDuration = supplyDuration;
            return this;
        }

        public SupplierDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in SupplierDTO");
            return this.instance;
        }
    }
}
