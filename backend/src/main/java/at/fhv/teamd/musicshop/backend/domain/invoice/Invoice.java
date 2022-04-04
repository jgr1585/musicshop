package at.fhv.teamd.musicshop.backend.domain.invoice;

import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoiceID_generator")
    @SequenceGenerator(name="invoiceID_generator", sequenceName = "invoice_sequence", allocationSize=10000)
    private int invoiceNo;

    @Enumerated(EnumType.STRING)
    private final PaymentMethod paymentMethod;

    @OneToMany(cascade = CascadeType.ALL)
    private final Set<LineItem> lineItems;

    @Column
    private final Customer assignedCustomer;

    public Invoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Customer assignedCustomer) {
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.lineItems = Objects.requireNonNull(lineItems);
        this.assignedCustomer = assignedCustomer;
    }
}
