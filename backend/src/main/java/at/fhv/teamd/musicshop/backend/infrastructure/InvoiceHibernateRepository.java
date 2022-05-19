package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.invoice.Invoice;
import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.library.DTO.InvoiceDTO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InvoiceHibernateRepository implements InvoiceRepository {

    // package-private constructor to enable initialization only through same package classes
    InvoiceHibernateRepository() {
    }

    @Override
    public Long addInvoice(Invoice invoice) {
        Objects.requireNonNull(invoice);

        EntityManager em = PersistenceManager.getEntityManagerInstance();
        em.getTransaction().begin();
        em.persist(invoice);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return invoice.getId();
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
    public Optional<Invoice> findInvoiceByLineItemId(Long lineItemId) {
        Objects.requireNonNull(lineItemId);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i ", Invoice.class);

        Optional<Invoice> invoiceToReturn = query.getResultList().stream()
                .filter(invoice -> invoice.getLineItems().stream().anyMatch(lineItem -> lineItem.getId() == lineItemId))
                .findFirst();

        em.close();

        return invoiceToReturn;
    }

    @Override
    public List<Invoice> findInvoicesByCustomerNo(int customerNo) {
        Objects.requireNonNull(customerNo);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i WHERE i.customerNo =:customerNo", Invoice.class);

        query.setParameter("customerNo", customerNo);

        List<Invoice> invoices = query.getResultList();

        em.close();

        return invoices;
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
