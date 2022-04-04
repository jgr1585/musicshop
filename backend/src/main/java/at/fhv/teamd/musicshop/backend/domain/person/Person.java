package at.fhv.teamd.musicshop.backend.domain.person;

import lombok.Getter;

import javax.persistence.Column;
import java.util.Objects;

@Getter
public abstract class Person {
    @Column
    private final String firstname;

    @Column
    private final String lastname;

    public Person(String firstname, String lastname) {
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
    }
}
