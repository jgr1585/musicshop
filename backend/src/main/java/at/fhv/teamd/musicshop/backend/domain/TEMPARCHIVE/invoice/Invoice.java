package at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.invoice;

import at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;

import java.util.List;
import java.util.Objects;

public class Invoice {
    private final int invoiceNo;
    private final PaymentMethod paymentMethod;

    private final List<LineItem> lineItems;
    private final Customer assignedCustomer;
    private final Employee createdByEmployee;

    public Invoice(int invoiceNo, PaymentMethod paymentMethod, List<LineItem> lineItems, Customer assignedCustomer, Employee createdByEmployee) {
        this.invoiceNo = invoiceNo;
        this.paymentMethod = Objects.requireNonNull(paymentMethod);
        this.lineItems = Objects.requireNonNull(lineItems);
        this.assignedCustomer = assignedCustomer;
        this.createdByEmployee = createdByEmployee;
    }
}
