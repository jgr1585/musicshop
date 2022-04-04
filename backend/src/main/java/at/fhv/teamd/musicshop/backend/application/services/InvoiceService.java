package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;

import java.util.Set;

public class InvoiceService {
    // TODO: static?
    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Employee createdByEmployee) {
        System.out.println("created new invoice");
        new Invoice(paymentMethod, lineItems, null, createdByEmployee);
    }
    public static void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, Customer assignedCustomer, Employee createdByEmployee) {
        System.out.println("created new invoice");
        new Invoice(paymentMethod, lineItems, assignedCustomer, createdByEmployee);
    }
}
