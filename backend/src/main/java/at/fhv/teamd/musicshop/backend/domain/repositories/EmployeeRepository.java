package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.backend.domain.user.Employee;

import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository {
    Optional<Employee> findEmployeeByUserName(String userName);
}
