package at.fhv.teamd.musicshop.backend.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeHibernateRepositoryTest {
    private EmployeeHibernateRepository employeeHibernateRepository;

    @BeforeEach
    void init() {
        employeeHibernateRepository = new EmployeeHibernateRepository();
    }

    @Test
    void searchCustomerById() {
    }
}