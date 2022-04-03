package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@DiscriminatorValue("Album")
public class Album extends Article {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Song> songs;

    protected Album() {
    }

    public Album(String title, String label, LocalDate releaseDate, String genre, String descriptorName, String musicbrainzId, Set<Medium> mediums, Set<Song> songs) {
        super(title, label, releaseDate, genre, descriptorName, musicbrainzId, mediums);

        this.songs = songs; // already checked for non-null @method:getArtistsFromSongs
    }
}
