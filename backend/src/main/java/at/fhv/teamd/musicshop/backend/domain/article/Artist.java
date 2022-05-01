package at.fhv.teamd.musicshop.backend.domain.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Artist {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private UUID uuid;

    @Column
    private String name;

    protected Artist() {}

    public Artist(String name) {
        this.uuid = UUID.randomUUID();
        this.name = Objects.requireNonNull(name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id && Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
