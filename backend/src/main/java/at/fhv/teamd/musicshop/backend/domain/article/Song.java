package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.medium.MediumType;
import lombok.Getter;

import javax.annotation.processing.Generated;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@DiscriminatorValue("Song")
public class Song extends Article {
    @Column
    private Duration length;

    @Column
    private Set<Artist> artists;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String descriptorName, String musicbrainzId, Set<Medium> mediums, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, descriptorName, musicbrainzId, mediums);

        this.length = Objects.requireNonNull(length);
        this.artists = Objects.requireNonNull(artists);
    }
}
