package at.fhv.teamd.musicshop.library.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class ShoppingCartDTO implements Serializable {
    private static final long serialVersionUID = -4652034296336248491L;

    private Set<LineItemDTO> lineItems;

    private BigDecimal totalAmount;

    public static ShoppingCartDTO.Builder builder() {
        return new ShoppingCartDTO.Builder();
    }

    public Set<LineItemDTO> lineItems() {
        return Collections.unmodifiableSet(this.lineItems);
    }

    public BigDecimal totalAmount() {
        return this.totalAmount;
    }

    private ShoppingCartDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartDTO that = (ShoppingCartDTO) o;
        return Objects.equals(lineItems, that.lineItems) && Objects.equals(totalAmount, that.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineItems, totalAmount);
    }

    public static class Builder {
        private final ShoppingCartDTO instance;

        private Builder() {
            this.instance = new ShoppingCartDTO();
        }

        public ShoppingCartDTO.Builder withLineItems(Set<LineItemDTO> lineItemDTOs) {
            this.instance.lineItems = lineItemDTOs;
            this.instance.totalAmount = calculateTotalAmount(lineItemDTOs);
            return this;
        }

        public ShoppingCartDTO build() {
            return this.instance;
        }
    }

    private static BigDecimal calculateTotalAmount (Set<LineItemDTO> lineItems) {
        BigDecimal value = new BigDecimal(0);
        for (LineItemDTO lineItem : lineItems) {
            value = value.add(lineItem.totalPrice());
        }
        return value;
    }
}
