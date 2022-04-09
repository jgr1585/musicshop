package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.person.Customer;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.repositories.ArticleRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

import java.util.Set;

public class InvoiceService {
    private static InvoiceRepository invoiceRepository;

    InvoiceService() {
        invoiceRepository = RepositoryFactory.getInvoiceRepositoryInstance();
    }

    // TODO: notification on client
    public void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems) {
        System.out.println("created new invoice");
        invoiceRepository.addInvoice(new Invoice(paymentMethod, lineItems));
    }
    public void createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, int assignedCustomer) {
        System.out.println("created new invoice");
        invoiceRepository.addInvoice(new Invoice(paymentMethod, lineItems, assignedCustomer));

    }
}
