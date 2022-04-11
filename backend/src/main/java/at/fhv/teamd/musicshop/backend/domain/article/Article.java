package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication")
public abstract class Article {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String title;

    @Column
    private String label;

    @Column
    private LocalDate releaseDate;

    @Column
    private String genre;

    @Column
    private String musicbrainzId;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Artist> artists;

    protected Article() {
    }

    protected Article(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Set<Artist> artists) {
        this.title = Objects.requireNonNull(title);
        this.label = Objects.requireNonNull(label);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.genre = Objects.requireNonNull(genre);
        this.musicbrainzId = Objects.requireNonNull(musicbrainzId);
        this.artists = Objects.requireNonNull(artists);
    }
}
