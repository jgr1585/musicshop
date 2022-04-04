package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;

import java.util.Set;

public class InvoiceService {

    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Employee createdByEmployee) {
        new Invoice(paymentMethod, lineItems, null, createdByEmployee);
    }
    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Customer assignedCustomer, Employee createdByEmployee) {
        new Invoice(paymentMethod, lineItems, assignedCustomer, createdByEmployee);
    }
}
