package at.fhv.teamd.musicshop.backend.domain.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Employee {
    @Id
    private String userName;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    protected Employee() {
    }

    public Employee(String userName, String firstname, String lastname, UserRole userRole) {
        this.userName = userName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userRole = userRole;
    }
}
