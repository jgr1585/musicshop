package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_of_publication")
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
    private String descriptorName;

    @Column
    private String musicbrainzId;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Medium> mediums;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Artist> artists;

    protected Article() {
    }

    protected Article(String title, String label, LocalDate releaseDate, String genre, String descriptorName, String musicbrainzId, Set<Medium> mediums, Set<Artist> artists) {
        this.title = Objects.requireNonNull(title);
        this.label = Objects.requireNonNull(label);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.genre = Objects.requireNonNull(genre);
        this.descriptorName = Objects.requireNonNull(descriptorName);
        this.musicbrainzId = Objects.requireNonNull(musicbrainzId);
        this.mediums = Objects.requireNonNull(mediums);
        this.mediums.forEach(medium -> medium.appendArticle(this));
        this.artists = Objects.requireNonNull(artists);
    }

    public Set<Long> getMediumIDs() {
        return mediums.stream()
                .map(Medium::getId)
                .collect(Collectors.toUnmodifiableSet());
    }
}
