package at.fhv.teamd.musicshop.backend.domain.TEMPARCHIVE.person;

import java.util.Objects;

public abstract class Person {
    private final String firstname;
    private final String lastname;

    public Person(String firstname, String lastname) {
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
    }
}
