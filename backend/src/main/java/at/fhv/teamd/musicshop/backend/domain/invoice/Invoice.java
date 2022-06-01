package at.fhv.teamd.musicshop.backend.domain.invoice;

import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoiceID_generator")
    @SequenceGenerator(name = "invoiceID_generator", sequenceName = "invoice_sequence", initialValue = 100, allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private UUID uuid;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<LineItem> lineItems;

    @Column
    private BigDecimal totalPrice;

    @Column
    private Integer customerNo;

    protected Invoice() {
    }

    private Invoice(Set<LineItem> lineItems, Integer customerNo) {
        this.lineItems = Objects.requireNonNull(lineItems);
        this.totalPrice = calculateTotalPrice(lineItems);
        this.customerNo = customerNo;
        this.uuid = UUID.nameUUIDFromBytes(Integer.toString(Objects.hash(lineItems, customerNo)).getBytes());
    }

    private Invoice(Set<LineItem> lineItems) {
        this(lineItems, null);
    }

    public static Invoice of(Set<LineItem> lineItems, int customerNo) {
        return new Invoice(lineItems, customerNo);
    }

    public static Invoice of(Set<LineItem> lineItems) {
        return new Invoice(lineItems);
    }

    private BigDecimal calculateTotalPrice(Set<LineItem> lineItems) {
        totalPrice = BigDecimal.valueOf(0);
        for (LineItem lineItem : lineItems) {
            totalPrice = totalPrice.add(lineItem.getTotalPrice());
        }
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return uuid.equals(invoice.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
