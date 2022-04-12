package at.fhv.teamd.musicshop.library.DTO;

import java.util.Objects;
import java.util.Set;

public class InvoiceDTO {

    private Long id;
    private String paymentMethod;
    private Set<LineItemDTO> lineItems;
    private CustomerDTO customer;

    public Long id() {
        return this.id;
    }

    public String paymentMethod() {
        return this.paymentMethod;
    }

    public Set<LineItemDTO> lineItems() {
        return this.lineItems;
    }

    public CustomerDTO customer() {
        return this.customer;
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
                String paymentMethod,
                Set<LineItemDTO> lineItems,
                CustomerDTO customer
        ) {
            this.instance.id = id;
            this.instance.paymentMethod = paymentMethod;
            this.instance.lineItems = lineItems;
            this.instance.customer = customer;
            return this;
        }

        public InvoiceDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in InvoiceDTO");
            return this.instance;
        }
    }
}
