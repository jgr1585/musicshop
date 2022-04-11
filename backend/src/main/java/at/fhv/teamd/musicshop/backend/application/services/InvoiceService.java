package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.invoice.PaymentMethod;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.domain.shoppingcart.LineItem;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;

import java.util.Set;

public class InvoiceService {
    private static InvoiceRepository invoiceRepository;

    InvoiceService() {
        invoiceRepository = RepositoryFactory.getInvoiceRepositoryInstance();
    }

    public boolean createInvoice(PaymentMethod paymentMethod, Set<LineItem> lineItems, int assignedCustomer) {
        try {
            if (assignedCustomer != 0) {
                invoiceRepository.addInvoice(new Invoice(paymentMethod, lineItems, assignedCustomer));
            } else {
                invoiceRepository.addInvoice(new Invoice(paymentMethod, lineItems));
            }
            System.out.println("created new invoice");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
