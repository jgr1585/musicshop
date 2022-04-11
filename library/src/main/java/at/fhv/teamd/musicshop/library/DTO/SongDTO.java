package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class SongDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 5897685238657995442L;

    private Long id;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Duration length;
    private Set<ArtistDTO> artists;

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

    public Set<MediumDTO> mediums() {
        return new HashSet<>();
    }

    public Duration length() {
        return length;
    }

    public Set<ArtistDTO> artists() {
        return Collections.unmodifiableSet(artists);
    }

    private SongDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongDTO songDTO = (SongDTO) o;
        return Objects.equals(id, songDTO.id) && Objects.equals(title, songDTO.title) && Objects.equals(label, songDTO.label) && Objects.equals(releaseDate, songDTO.releaseDate) && Objects.equals(genre, songDTO.genre) && Objects.equals(musicbrainzId, songDTO.musicbrainzId) && Objects.equals(length, songDTO.length) && Objects.equals(artists, songDTO.artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, label, releaseDate, genre, musicbrainzId, length, artists);
    }

    public static class Builder {
        private final SongDTO instance;

        private Builder() {
            this.instance = new SongDTO();
        }

        public SongDTO.Builder withSongData(
                Long id,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                Duration length,
                Set<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.length = length;
            this.instance.artists = artists;
            return this;
        }

        public SongDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in SongDTO");
            return this.instance;
        }
    }
}
