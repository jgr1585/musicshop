package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ShoppingCartDTO implements Serializable {
    private static final long serialVersionUID = -4652034296336248491L;

    private Set<LineItemDTO> lineItems;

    public static ShoppingCartDTO.Builder builder() {
        return new ShoppingCartDTO.Builder();
    }

    public Set<LineItemDTO> lineItems() {
        return Collections.unmodifiableSet(this.lineItems);
    }

    private ShoppingCartDTO() {
    }

    public static class Builder {
        private ShoppingCartDTO instance;

        private Builder() {
            this.instance = new ShoppingCartDTO();
        }

        public ShoppingCartDTO.Builder withLineItems(Set<LineItemDTO> lineItemDTOs) {
            this.instance.lineItems = lineItemDTOs;
            return this;
        }

        public ShoppingCartDTO build() {
            return this.instance;
        }
    }
}
