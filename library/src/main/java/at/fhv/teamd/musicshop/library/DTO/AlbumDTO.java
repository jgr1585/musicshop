package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class AlbumDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = -8086177709046734795L;

    private Long id;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Set<ArtistDTO> artists;
    private Set<MediumDTO> mediums;
    private Set<SongDTO> songs;

    public static AlbumDTO.Builder builder() {
        return new AlbumDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public String title() {
        return this.title;
    }

    public String label() {
        return this.label;
    }

    public LocalDate releaseDate() {
        return this.releaseDate;
    }

    public String genre() {
        return this.genre;
    }

    public String musicbrainzId() {
        return this.musicbrainzId;
    }

    public Set<ArtistDTO> artists() {
        return Collections.unmodifiableSet(artists);
    }

    public Set<MediumDTO> mediums() {
        return Collections.unmodifiableSet(mediums);
    }

    public Set<SongDTO> songs() {
        return Collections.unmodifiableSet(songs);
    }

    private AlbumDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumDTO albumDTO = (AlbumDTO) o;
        return id.equals(albumDTO.id) && title.equals(albumDTO.title) && label.equals(albumDTO.label) && releaseDate.equals(albumDTO.releaseDate) && genre.equals(albumDTO.genre) && musicbrainzId.equals(albumDTO.musicbrainzId) && artists.equals(albumDTO.artists) && mediums.equals(albumDTO.mediums) && songs.equals(albumDTO.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, label, releaseDate, genre, musicbrainzId, artists, mediums, songs);
    }

    public static class Builder {
        private final AlbumDTO instance;

        private Builder() {
            this.instance = new AlbumDTO();
        }

        public AlbumDTO.Builder withAlbumData(
                Long id,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                Set<ArtistDTO> artists,
                Set<MediumDTO> mediums,
                Set<SongDTO> songs
        ) {
            this.instance.id = id;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.artists = artists;
            this.instance.mediums = mediums;
            this.instance.songs = songs;
            return this;
        }

        public AlbumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in AlbumDTO");
            return this.instance;
        }
    }
}
