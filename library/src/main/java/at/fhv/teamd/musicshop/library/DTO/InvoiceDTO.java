package at.fhv.teamd.musicshop.library.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public final class InvoiceDTO implements Serializable {

    @JsonProperty(required = true)
    private Long id;

    @JsonProperty(required = true)
    private Set<LineItemDTO> lineItems;

    @JsonProperty(required = true)
    private BigDecimal totalPrice;

    @JsonProperty(required = true)
    private Integer customerNo;

    public Long id() {
        return this.id;
    }

    public BigDecimal totalPrice() {
        return this.totalPrice;
    }

    public Set<LineItemDTO> lineItems() {
        return this.lineItems;
    }

    public Integer customerNo() {
        return this.customerNo;
    }

    public static InvoiceDTO.Builder builder() {
        return new InvoiceDTO.Builder();
    }

    private InvoiceDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDTO that = (InvoiceDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(lineItems, that.lineItems) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(customerNo, that.customerNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineItems, totalPrice, customerNo);
    }

    public static class Builder {
        private final InvoiceDTO instance;

        private Builder() {
            this.instance = new InvoiceDTO();
        }

        public InvoiceDTO.Builder withInvoiceData(
                Long id,
                Set<LineItemDTO> lineItems,
                BigDecimal totalPrice,
                Integer customerNo
        ) {
            this.instance.id = id;
            this.instance.lineItems = lineItems;
            this.instance.totalPrice = totalPrice;
            this.instance.customerNo = customerNo;
            return this;
        }

        public InvoiceDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in InvoiceDTO");
            return this.instance;
        }
    }
}
