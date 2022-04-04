package at.fhv.teamd.musicshop.backend.domain.person;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
public class UserAccount {
    @Id
    private final String userId;

    @Column
    private final String username;

    @OneToOne
    private final Person person;

    @Enumerated(EnumType.STRING)
    private final UserRole userRole;

    public UserAccount(String userId, String username, Person person, UserRole userRole) {
        this.userId = Objects.requireNonNull(userId);
        this.username = Objects.requireNonNull(username);
        this.person = Objects.requireNonNull(person);
        this.userRole = Objects.requireNonNull(userRole);
    }
}
