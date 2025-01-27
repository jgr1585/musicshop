package at.fhv.teamd.musicshop.backend.domain.employee;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.library.permission.UserRole;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;
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

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Topic> subscribedTopics;

    protected Employee() {
    }

    public Employee(String userName, String firstname, String lastname, Set<UserRole> userRoles, Set<Topic> subscribedTopics) {
        this.userName = Objects.requireNonNull(userName);
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
        this.userRoles = Objects.requireNonNull(userRoles);
        this.subscribedTopics = Objects.requireNonNull(subscribedTopics);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return userName.equals(employee.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
