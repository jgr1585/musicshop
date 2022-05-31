package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.repositories.CustomerRepository;
import at.fhv.teamd.musicshop.backend.domain.customer.Customer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

public class CustomerHibernateRepository implements CustomerRepository {

    @Override
    @Transactional
    public Optional<Customer> findCustomerByUserName(String userName) {
        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Customer> query = em.createQuery(
                "SELECT c FROM Customer c WHERE c.userName=:userName", Customer.class);

        query.setParameter("userName", userName);

        Optional<Customer> customerOpt;

        try {
            customerOpt = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            customerOpt = Optional.empty();
        }

        em.close();
        return customerOpt;
    }
}
