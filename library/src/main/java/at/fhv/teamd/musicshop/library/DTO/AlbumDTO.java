package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class AlbumDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 1577561200659149099L;

    private Long id;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Set<MediumDTO> mediums;
    private Set<SongDTO> songs;
    private Set<ArtistDTO> artists;

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

    public Set<MediumDTO> mediums() {
        return Collections.unmodifiableSet(mediums);
    }

    public Set<SongDTO> songs() {
        return Collections.unmodifiableSet(songs);
    }

    public Set<ArtistDTO> artists() {
        return Collections.unmodifiableSet(artists);
    }

    private AlbumDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumDTO albumDTO = (AlbumDTO) o;
        return Objects.equals(id, albumDTO.id) && Objects.equals(title, albumDTO.title) && Objects.equals(label, albumDTO.label) && Objects.equals(releaseDate, albumDTO.releaseDate) && Objects.equals(genre, albumDTO.genre) && Objects.equals(musicbrainzId, albumDTO.musicbrainzId) && Objects.equals(mediums, albumDTO.mediums) && Objects.equals(songs, albumDTO.songs) && Objects.equals(artists, albumDTO.artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, label, releaseDate, genre, musicbrainzId, mediums, songs, artists);
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
                Set<MediumDTO> mediums,
                Set<SongDTO> songs,
                Set<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.mediums = mediums;
            this.instance.songs = songs;
            this.instance.artists = artists;
            return this;
        }

        public AlbumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in AlbumDTO");
            return this.instance;
        }
    }
}
