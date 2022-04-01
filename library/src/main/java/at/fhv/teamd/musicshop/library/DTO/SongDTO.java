package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class SongDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 5897685238657995442L;

    private Long id;
    private String descriptorName;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private Map<String, AnalogMediumDTO> analogMedium;
    private Duration length;
    private List<ArtistDTO> artists;

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

    public Map<String, AnalogMediumDTO> analogMedium() { return Collections.unmodifiableMap(analogMedium); }

    public Duration length() {
        return length;
    }

    public List<ArtistDTO> artists() {
        return Collections.unmodifiableList(artists);
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
                Map<String, AnalogMediumDTO> analogMedium,
                Duration length,
                List<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.descriptorName = descriptorName;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.analogMedium = analogMedium;
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
