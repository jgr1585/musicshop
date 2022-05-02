package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_of_publication")
public abstract class Article {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private UUID uuid;

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
        this.uuid = UUID.randomUUID();
        this.title = Objects.requireNonNull(title);
        this.label = Objects.requireNonNull(label);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.genre = Objects.requireNonNull(genre);
        this.musicbrainzId = Objects.requireNonNull(musicbrainzId);
        this.artists = Objects.requireNonNull(artists);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return uuid.equals(article.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
