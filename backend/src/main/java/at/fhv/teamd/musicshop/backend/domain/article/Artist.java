package at.fhv.teamd.musicshop.backend.domain.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Artist {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    protected Artist() {}

    public Artist(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
