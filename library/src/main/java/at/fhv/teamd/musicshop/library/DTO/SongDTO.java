package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class SongDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 6177922003024617967L;

    private Long id;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Set<ArtistDTO> artists;
    private Duration length;
    private Set<AlbumDTO> albums;

    public static SongDTO.Builder builder() {
        return new SongDTO.Builder();
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

    public String musicbrainzId() { return this.musicbrainzId; }

    public Set<ArtistDTO> artists() {
        return Collections.unmodifiableSet(artists);
    }

    public Duration length() {
        return length;
    }

    public Set<AlbumDTO> albums() {
        return Collections.unmodifiableSet(albums);
    }

    private SongDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongDTO songDTO = (SongDTO) o;
        return id.equals(songDTO.id) && title.equals(songDTO.title) && label.equals(songDTO.label) && releaseDate.equals(songDTO.releaseDate) && genre.equals(songDTO.genre) && musicbrainzId.equals(songDTO.musicbrainzId) && artists.equals(songDTO.artists) && Objects.equals(length, songDTO.length) && Objects.equals(albums, songDTO.albums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, label, releaseDate, genre, musicbrainzId, artists, length, albums);
    }

    public static class Builder {
        private final SongDTO instance;

        private Builder() {
            this.instance = new SongDTO();
        }

        public SongDTO.Builder withArticleSpecificData(
                Long id,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                Set<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.artists = artists;
            return this;
        }

        public SongDTO.Builder withSongSpecificData(
                Duration length,
                Set<AlbumDTO> albums
        ) {
            this.instance.length = length;
            this.instance.albums = albums;
            return this;
        }

        public SongDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in SongDTO");
            return this.instance;
        }
    }
}
