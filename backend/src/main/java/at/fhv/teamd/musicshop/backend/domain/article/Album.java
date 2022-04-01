package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMediumType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Album extends Article {
    @OneToMany(cascade= CascadeType.ALL)
    private List<Song> songs;

    protected Album() {
    }

    public Album(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Map<AnalogMediumType, AnalogMedium> analogMedium, List<Song> songs) {
        super(title, label, releaseDate, genre, musicbrainzId, analogMedium, getArtistsFromSongs(songs));

        this.songs = songs; // already checked for non-null @method:getArtistsFromSongs
    }

    private static List<Artist> getArtistsFromSongs(List<Song> songs) {
        List<Artist> artists = new ArrayList<>();

        Objects.requireNonNull(songs).forEach(song -> {
            song.getArtists().forEach(artist -> {
                if (!artists.contains(artist)) {
                    artists.add(artist);
                }
            });
        });

        return artists;
    }

    public List<Song> getSongs() {
        return Collections.unmodifiableList(songs);
    }
}
