package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;

import javax.persistence.EntityManager;
import java.util.Objects;

public class InvoiceHibernateRepository implements InvoiceRepository {

    // package-private constructor to enable initialization only through same package classes
    InvoiceHibernateRepository() {
    }

    @Override
    public void addInvoice(Invoice invoice) {
        Objects.requireNonNull(invoice);

        EntityManager em = PersistenceManager.getEntityManagerInstance();
        em.getTransaction().begin();
        em.persist(invoice);
        em.flush();
        em.getTransaction().commit();
        em.close();

        System.out.println("invoice persisted");
    }
}
