package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@DiscriminatorValue("Song")
public class Song extends Article {

    @Column
    private Duration length;

    @ManyToMany(mappedBy="songs")
    private Set<Album> albums;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, musicbrainzId, artists);
        this.length = Objects.requireNonNull(length);
    }
}
