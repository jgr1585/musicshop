package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;

import java.util.Set;

public class InvoiceService {
    // TODO: static?
    // TODO: notification on client
    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems) {
        System.out.println("created new invoice");
        new Invoice(paymentMethod, lineItems, null);
    }
    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Customer assignedCustomer) {
        System.out.println("created new invoice");
        new Invoice(paymentMethod, lineItems, assignedCustomer);
    }
}
