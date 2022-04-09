package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity(name = "Song")
@DiscriminatorValue("Song")
public class Song extends Article {
    @Column
    private Duration length;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, musicbrainzId, artists);
        this.length = Objects.requireNonNull(length);
    }
}
