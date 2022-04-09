package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Entity(name = "Album")
@DiscriminatorValue("Album")
public class Album extends Article {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Song> songs;

    protected Album() {
    }

    public Album(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Set<Song> songs) {
        super(title, label, releaseDate, genre, musicbrainzId, findArtistsFromSongs(songs));
        this.songs = songs; // already checked for non-null @method:getArtistsFromSongs
    }

    private static Set<Artist> findArtistsFromSongs(Set<Song> songs) {
        Set<Artist> artists = new HashSet<>();
        songs.forEach(song -> artists.addAll(song.getArtists()));
        return artists;
    }
}
