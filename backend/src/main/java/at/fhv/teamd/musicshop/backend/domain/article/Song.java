package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@DiscriminatorValue("Song")
public class Song extends Article {

    @Column
    private Duration length;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL)
    private Set<Album> albums;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, musicbrainzId, artists);
        this.length = Objects.requireNonNull(length);
    }

    public Song(UUID uuid, String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Duration length, Set<Artist> artists) {
        super(uuid, title, label, releaseDate, genre, musicbrainzId, artists);
        this.length = Objects.requireNonNull(length);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
