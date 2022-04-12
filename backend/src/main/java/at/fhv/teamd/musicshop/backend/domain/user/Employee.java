package at.fhv.teamd.musicshop.backend.domain.user;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
public class Employee {
    @Id
    @GeneratedValue
    private int employeeNo;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @ManyToMany
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles;

    protected Employee() {
    }

    public Employee(String firstname, String lastname, Set<UserRole> userRoles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.userRoles = userRoles;
    }
}