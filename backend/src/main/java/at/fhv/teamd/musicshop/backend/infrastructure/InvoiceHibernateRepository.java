package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    @Transactional
    public Optional<Invoice> findInvoiceById(Long id) {
        Objects.requireNonNull(id);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i " +
                "WHERE i.id=:id", Invoice.class);

        query.setParameter("id", id);

        Optional<Invoice> invoiceOpt;

        try {
            invoiceOpt = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            invoiceOpt = Optional.empty();
        }

        em.close();

        return invoiceOpt;
    }

    @Override
    @Transactional
    public Invoice findInvoiceByLineItemId(Long lineItemId) {
        Objects.requireNonNull(lineItemId);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i ", Invoice.class);

        Invoice invoiceToReturn = query.getResultList().stream()
                .filter(invoice -> invoice.getLineItems().stream().anyMatch(lineItem -> lineItem.getId() == lineItemId))
                .findFirst().orElseThrow();

        em.close();

        return invoiceToReturn;
    }

    @Override
    @Transactional
    public void update(Invoice invoice) {
        Objects.requireNonNull(invoice);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        em.getTransaction().begin();

        em.merge(invoice);

        em.getTransaction().commit();

        em.close();
    }
}
