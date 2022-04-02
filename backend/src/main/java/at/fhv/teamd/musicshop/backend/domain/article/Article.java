package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
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
    @OneToMany(mappedBy = "article")
    private Set<Medium> mediums;

    protected Article() {
    }

    protected Article(String title, String label, LocalDate releaseDate, String genre, String descriptorName, String musicbrainzId, Set<Medium> mediums) {
        this.title = Objects.requireNonNull(title);
        this.label = Objects.requireNonNull(label);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.genre = Objects.requireNonNull(genre);
        this.descriptorName = Objects.requireNonNull(descriptorName);
        this.musicbrainzId = Objects.requireNonNull(musicbrainzId);
        this.mediums = Objects.requireNonNull(mediums);
    }

    public Set<Long> getMediumIDs() {
        return mediums.stream()
                .map(Medium::getId)
                .collect(Collectors.toUnmodifiableSet());
    }
}
