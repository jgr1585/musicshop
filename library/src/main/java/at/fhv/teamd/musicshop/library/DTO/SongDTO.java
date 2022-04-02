package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public final class SongDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 5897685238657995442L;

    private Long id;
    private String descriptorName;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Set<MediumDTO> mediums;
    private Duration length;
    private Set<ArtistDTO> artists;

    public static SongDTO.Builder builder() {
        return new SongDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public String descriptorName() { return this.descriptorName; }

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

    public Set<MediumDTO> mediums() { return Collections.unmodifiableSet(mediums); }

    public Duration length() {
        return length;
    }

    public Set<ArtistDTO> artists() {
        return Collections.unmodifiableSet(artists);
    }

    private SongDTO() {
    }

    public static class Builder {
        private SongDTO instance;

        private Builder() {
            this.instance = new SongDTO();
        }

        public SongDTO.Builder withSongData(
                Long id,
                String descriptorName,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                Set<MediumDTO> mediums,
                Duration length,
                Set<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.descriptorName = descriptorName;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.mediums = mediums;
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
