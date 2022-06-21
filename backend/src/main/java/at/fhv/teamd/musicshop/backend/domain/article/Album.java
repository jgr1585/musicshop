package at.fhv.teamd.musicshop.backend.domain.article;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@DiscriminatorValue("Album")
public class Album extends Article {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Song> songs;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private Set<Medium> mediums;

    protected Album() {
    }

    public Album(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Set<Song> songs) {
        super(title, label, releaseDate, genre, musicbrainzId, findArtistsFromSongs(songs));
        this.songs = songs; // already checked for non-null @method:findArtistsFromSongs
    }

    private static Set<Artist> findArtistsFromSongs(Set<Song> songs) {
        Objects.requireNonNull(songs);
        Set<Artist> artists = new HashSet<>();
        songs.forEach(song -> artists.addAll(song.getArtists()));
        return artists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
