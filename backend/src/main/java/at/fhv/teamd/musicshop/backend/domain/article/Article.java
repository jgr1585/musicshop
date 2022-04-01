package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMediumType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@MappedSuperclass
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

    // Use or remove references as needed later on when creating the domain model
    //private final DigitalMedium digitalMedium;
    @OneToMany(cascade= CascadeType.ALL)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<AnalogMediumType, AnalogMedium> analogMedium;
    @OneToMany(cascade=CascadeType.ALL)
    protected List<Artist> artists;

    protected Article() {}

    protected Article(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, /*, DigitalMedium digitalMedium, */ Map<AnalogMediumType, AnalogMedium> analogMedium, List<Artist> artists) {
        this.title = Objects.requireNonNull(title);
        this.label = Objects.requireNonNull(label);
        this.releaseDate = Objects.requireNonNull(releaseDate);
        this.genre = Objects.requireNonNull(genre);
        this.musicbrainzId = Objects.requireNonNull(musicbrainzId);
        //this.digitalMedium = Objects.requireNonNull(digitalMedium);
        this.analogMedium = Objects.requireNonNull(analogMedium);
        this.artists = Objects.requireNonNull(artists);
    }

    public long getId() {
        return id;
    }

    public String getDescriptorName() { return String.format("%s (%s)" , title, label.toUpperCase()); }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public String getMusicbrainzId() { return musicbrainzId; }

    public Map<AnalogMediumType, Long> getAnalogMediumIDs() {
        return analogMedium.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        m -> m.getKey(),
                        m -> m.getValue().getId()
                ));
    }

    public List<Artist> getArtists() {
        return Collections.unmodifiableList(artists);
    }
}
