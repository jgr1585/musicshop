package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.repositories.EmployeeRepository;
import at.fhv.teamd.musicshop.backend.domain.user.Employee;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

public class EmployeeHibernateRepository implements EmployeeRepository {

    @Override
    @Transactional
    public Optional<Employee> findEmployeeByUserName(String userName) {
        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.userName=:userName", Employee.class);

        query.setParameter("userName", userName);

        Optional<Employee> employeeOpt;

        try {
            employeeOpt = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            employeeOpt = Optional.empty();
        }

        em.close();
        return employeeOpt;
    }

}
