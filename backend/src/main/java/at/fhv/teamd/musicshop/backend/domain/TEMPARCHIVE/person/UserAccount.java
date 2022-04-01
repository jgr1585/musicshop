package at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.person;

import java.util.Objects;

public class UserAccount {
    private final String userId;
    private final String username;

    private final Person person;

    public UserAccount(String userId, String username, Person person) {
        this.userId = Objects.requireNonNull(userId);
        this.username = Objects.requireNonNull(username);
        this.person = Objects.requireNonNull(person);
    }
}
