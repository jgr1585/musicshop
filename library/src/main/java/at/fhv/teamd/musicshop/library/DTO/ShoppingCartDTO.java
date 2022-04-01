package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ShoppingCartDTO implements Serializable {
    private static final long serialVersionUID = -4652034296336248491L;

    private List<LineItemDTO> lineItems;

    public static ShoppingCartDTO.Builder builder() {
        return new ShoppingCartDTO.Builder();
    }

    public List<LineItemDTO> lineItems() {
        return Collections.unmodifiableList(this.lineItems);
    }

    private ShoppingCartDTO() {
    }

    public static class Builder {
        private ShoppingCartDTO instance;

        private Builder() {
            this.instance = new ShoppingCartDTO();
        }

        public ShoppingCartDTO.Builder withLineItems(
                List<LineItemDTO> lineItemDTOs
        ) {
            this.instance.lineItems = lineItemDTOs;
            return this;
        }

        public ShoppingCartDTO build() {
            return this.instance;
        }
    }
}
