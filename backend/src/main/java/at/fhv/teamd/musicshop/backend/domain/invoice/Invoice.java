package at.fhv.teamd.musicshop.backend.domain.invoice;

import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoiceID_generator")
    @SequenceGenerator(name="invoiceID_generator", sequenceName = "invoice_sequence", allocationSize=10000)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<LineItem> lineItems;

    @Column
    private BigDecimal totalPrice;

    @Column
    private Integer customerNo;

    protected Invoice() {
    }

    public Invoice(PaymentMethod paymentMethod, Set<LineItem> lineItems) {
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.lineItems = Objects.requireNonNull(lineItems);
        this.totalPrice = calculateTotalPrice(lineItems);
        this.customerNo = null;
    }

    public Invoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, int customerNo) {
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.lineItems = Objects.requireNonNull(lineItems);
        this.totalPrice = calculateTotalPrice(lineItems);
        this.customerNo = customerNo;
    }

    private BigDecimal calculateTotalPrice(Set<LineItem> lineItems) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (LineItem lineItem : lineItems) {
            totalPrice = totalPrice.add(lineItem.getTotalPrice());
        }
        return totalPrice;
    }
}
