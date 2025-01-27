package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.employee.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

class EmployeeHibernateRepositoryTest {
    private EmployeeHibernateRepository employeeHibernateRepository;

    @BeforeEach
    void init() {
        employeeHibernateRepository = new EmployeeHibernateRepository();

        BaseRepositoryData.init();
    }

    @Test
    void given_userName_when_findEmployeeByUserName_returnEmployee() {
        //given
        Set<Employee> employees = BaseRepositoryData.getEmployees();
        String userName = employees.iterator().next().getUserName();
        Optional<Employee> expectedEmployee = employees.stream()
                .filter(employee -> employee.getUserName().toLowerCase().contains(userName.toLowerCase()))
                .findFirst();

        //when
        Optional<Employee> actualEmployee = this.employeeHibernateRepository.findEmployeeByUserName(userName);

        //then
        Assertions.assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    void given_non_existing_userName_when_findEmployeeByUserName_emptyOptional() {
        //given
        Set<Employee> employees = BaseRepositoryData.getEmployees();
        String userName = employees.iterator().next().getUserName() + UUID.randomUUID() + UUID.randomUUID();
        Optional<Employee> expectedEmployee = Optional.empty();

        //when
        Optional<Employee> actualEmployee = this.employeeHibernateRepository.findEmployeeByUserName(userName);

        //then
        Assertions.assertEquals(expectedEmployee, actualEmployee);
    }
}