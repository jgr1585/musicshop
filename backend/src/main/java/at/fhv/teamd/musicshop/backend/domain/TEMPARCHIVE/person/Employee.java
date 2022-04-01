package at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.person;

import java.util.Set;

public class Employee extends Person {
    private final int employeeNo;

    private final Set<UserRole> userRoles;

    public Employee(String firstname, String lastname, int employeeNo, Set<UserRole> userRoles) {
        super(firstname, lastname);

        this.employeeNo = employeeNo;
        this.userRoles = userRoles;
    }
}
