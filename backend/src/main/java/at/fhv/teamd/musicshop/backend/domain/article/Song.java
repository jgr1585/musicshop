package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMediumType;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
public class Song extends Article {
    @Column
    private Duration length;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Map<AnalogMediumType, AnalogMedium> analogMedium, Duration length, List<Artist> artists) {
        super(title, label, releaseDate, genre, musicbrainzId, analogMedium, artists);

        this.length = Objects.requireNonNull(length);
    }

    public Duration getLength() {
        return length;
    }
}
