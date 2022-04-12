package at.fhv.teamd.musicshop.backend.domain.user;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Employee {
    @Id
    private String userName;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles;

    protected Employee() {
    }

    public Employee(String userName, String firstname, String lastname, Set<UserRole> userRoles) {
        this.userName = userName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userRoles = userRoles;
    }
}
