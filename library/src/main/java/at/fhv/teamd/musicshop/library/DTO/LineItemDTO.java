package at.fhv.teamd.musicshop.library.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class LineItemDTO implements Serializable {
    private static final long serialVersionUID = 7231320470816137658L;

    @JsonProperty(required = true)
    private Long id;

    @JsonProperty(required = true)
    private ArticleDTO article;

    @JsonProperty(required = true)
    private Integer quantity;

    @JsonProperty(required = true)
    private Integer quantityReturn;

    @JsonProperty(required = true)
    private BigDecimal price;

    @JsonProperty(required = true)
    private BigDecimal totalPrice;

    @JsonProperty(required = true)
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

    public Integer quantityReturn() {
        return this.quantityReturn;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItemDTO that = (LineItemDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(article, that.article) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(medium, that.medium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, article, quantity, price, totalPrice, medium);
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
                Integer quantityReturn,
                BigDecimal price,
                BigDecimal totalPrice,
                MediumDTO mediumDTO
        ) {
            this.instance.id = id;
            this.instance.article = articleDTO;
            this.instance.quantity = quantity;
            this.instance.quantityReturn = quantityReturn;
            this.instance.price = price;
            this.instance.totalPrice = totalPrice;
            this.instance.medium = mediumDTO;
            return this;
        }

        public LineItemDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.article, "article must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.quantity, "quantity must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.quantity, "quantityReturn must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.price, "price must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.totalPrice, "totalPrice must be set in LineItemDTO");
            Objects.requireNonNull(this.instance.medium, "mediumDTO must be set in LineItemDTO");
            return this.instance;
        }
    }
}
