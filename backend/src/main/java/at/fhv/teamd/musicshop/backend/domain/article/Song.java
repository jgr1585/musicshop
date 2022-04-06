package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@DiscriminatorValue("Song")
public class Song extends Article {
    @Column
    private Duration length;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String descriptorName, String musicbrainzId, Set<Medium> mediums, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, descriptorName, musicbrainzId, mediums, artists);
        this.length = Objects.requireNonNull(length);
    }
}
