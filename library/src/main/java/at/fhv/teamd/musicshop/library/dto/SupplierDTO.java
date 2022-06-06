package at.fhv.teamd.musicshop.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public final class SupplierDTO implements Serializable {
    private static final long serialVersionUID = 7220444567998280771L;

    @JsonProperty(required = true)
    private Long id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonSerialize(using = DurationSerializer.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierDTO that = (SupplierDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(supplyDuration, that.supplyDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, supplyDuration);
    }

    public static class Builder {
        private final SupplierDTO instance;

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
