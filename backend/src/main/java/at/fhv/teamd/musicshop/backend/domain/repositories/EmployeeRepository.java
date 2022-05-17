package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.employee.Employee;

import java.util.Optional;

public interface EmployeeRepository {
    Optional<Employee> findEmployeeByUserName(String userName);
}
