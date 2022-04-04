package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.person.Employee;

import java.util.Set;

public interface EmployeeRepository {
    Set<Employee> searchCustomerById(int customerId);
}
