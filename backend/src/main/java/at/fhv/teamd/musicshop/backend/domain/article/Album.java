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

    @OneToMany(mappedBy="album")
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

    //TODO: FIX
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Album)) return false;
//        if (!super.equals(o)) return false;
//        Album album = (Album) o;
//        return this.equalsWithoutSongs(album) && (this.songs == null || album.songs == null) ? this.songs == album.songs :this.songs.stream().allMatch(song -> album.songs.stream().anyMatch(song::equalsWithoutAlbums));
//    }
//
//    boolean equalsWithoutSongs(Album album) {
//        if (!super.equals(album)) return false;
//        return this.mediums.containsAll(album.mediums) && album.mediums.containsAll(this.mediums);
//    }

    //TODO: Fix
//    @Override
//    public int hashCode() {
//        return Objects.hash(this.hashCodeWithoutSongs(), this.songs == null ? "" : this.songs.stream().map(Song::hashCodeWithoutAlbums).collect(Collectors.toSet()));
//    }
//
//    int hashCodeWithoutSongs() {
//        return Objects.hash(super.hashCode(), mediums);
//    }
}
