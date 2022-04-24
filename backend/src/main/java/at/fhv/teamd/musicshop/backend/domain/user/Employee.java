package at.fhv.teamd.musicshop.backend.domain.user;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Topic> subscribedTopics;

    protected Employee() {
    }

    public Employee(String userName, String firstname, String lastname, UserRole userRole, Set<Topic> subscribedTopics) {
        this.userName = Objects.requireNonNull(userName);
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
        this.userRole = Objects.requireNonNull(userRole);
        this.subscribedTopics = Objects.requireNonNull(subscribedTopics);
    }
}
