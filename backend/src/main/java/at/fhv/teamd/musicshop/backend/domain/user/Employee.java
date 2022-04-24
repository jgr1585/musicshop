package at.fhv.teamd.musicshop.backend.domain.user;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import lombok.Getter;

import javax.persistence.*;
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

    public Employee(String userName, String firstname, String lastname, UserRole userRole) {
        this.userName = userName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userRole = userRole;
    }
}
