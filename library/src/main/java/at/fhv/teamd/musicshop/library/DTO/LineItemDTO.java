package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class LineItemDTO implements Serializable {
    private static final long serialVersionUID = 7231320470816137658L;

    private Long id;

    private ArticleDTO article;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private MediumDTO medium;

    public static LineItemDTO.Builder builder() {
        return new LineItemDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public ArticleDTO article() {
        return this.article;
    }

    public Integer quantity() {
        return this.quantity;
    }

    public BigDecimal price() {
        return this.price;
    }

    public BigDecimal totalPrice() {
        return this.totalPrice;
    }

    public MediumDTO medium() {
        return this.medium;
    }

    private LineItemDTO() {
    }

    public static class Builder {
        private final LineItemDTO instance;

        private Builder() {
            this.instance = new LineItemDTO();
        }

        public LineItemDTO.Builder withLineItemData(
                Long id,
                ArticleDTO articleDTO,
                Integer quantity,
                BigDecimal price,
                BigDecimal totalPrice,
                MediumDTO mediumDTO
        ) {
            this.instance.id = id;
            this.instance.article = articleDTO;
            this.instance.quantity = quantity;
            this.instance.price = price;
            this.instance.totalPrice = totalPrice;
            this.instance.medium = mediumDTO;
            return this;
        }

        public LineItemDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.article, "article must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.quantity, "quantity must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.price, "price must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.totalPrice, "totalPrice must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.medium, "mediumDTO must be set in LineItemDTO");
            return this.instance;
        }
    }
}
