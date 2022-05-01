package at.fhv.teamd.musicshop.backend.domain.article;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@DiscriminatorValue("Song")
public class Song extends Article {

    @Column
    private Duration length;

    @ManyToMany(mappedBy="songs")
    private Set<Album> albums;

    protected Song() {
    }

    public Song(String title, String label, LocalDate releaseDate, String genre, String musicbrainzId, Duration length, Set<Artist> artists) {
        super(title, label, releaseDate, genre, musicbrainzId, artists);
        this.length = Objects.requireNonNull(length);
    }

    //TODO: FIX
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Song)) return false;
//        if (!super.equals(o)) return false;
//        Song song = (Song) o;
//        return this.equalsWithoutAlbums(song) && (this.albums == null || song.albums == null) ? this.albums == song.albums : this.albums.stream().allMatch(album -> song.albums.stream().anyMatch(album::equalsWithoutSongs));
//    }
//
//    boolean equalsWithoutAlbums(Song song) {
//        if (!super.equals(song)) return false;
//        return Objects.equals(length, song.length);
//    }

    //TODO: FIX
//    @Override
//    public int hashCode() {
//        return Objects.hash(this.hashCodeWithoutAlbums(), this.albums == null ? "" : this.albums.stream().map(Album::hashCodeWithoutSongs).collect(Collectors.toSet()));
//    }
//
//    int hashCodeWithoutAlbums() {
//        return Objects.hash(super.hashCode(), length);
//    }
}
