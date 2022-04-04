package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.person.Employee;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EmployeeHibernateRepository implements EmployeeRepository {

    @Override
    @Transactional
    public Set<Employee> searchCustomerById(int id) {
        Objects.requireNonNull(id);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.id=:id", Employee.class);

        query.setParameter("id", id);

        Set<Employee> employees = new HashSet<>(query.getResultList());

        em.close();
        return employees;
    }
}
