package at.fhv.teamd.musicshop.backend.domain.person;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
public class Employee extends Person {
    @Id
    @GeneratedValue
    private int employeeNo;

    @ManyToMany
    @Enumerated(EnumType.STRING)
    private final Set<UserRole> userRoles;

    public Employee(String firstname, String lastname, Set<UserRole> userRoles) {
        super(firstname, lastname);
        this.userRoles = userRoles;
    }
}
