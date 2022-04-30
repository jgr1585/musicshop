package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class InvoiceDTO implements Serializable {

    private Long id;
    private Set<LineItemDTO> lineItems;
    private BigDecimal totalPrice;
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

    public static class Builder {
        private InvoiceDTO instance;

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
