package at.fhv.teamd.musicshop.library.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class SongDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = -8929295739018385391L;

    private Long id;
    private UUID uuid;
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

    // no json property
    public UUID uuid() { return this.uuid; }

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

    @JsonProperty(required = true)
    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonSerialize(using = DurationSerializer.class)
    public Duration length() {
        return length;
    }

    @JsonProperty(required = true)
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
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
        return this.equalsWithoutAlbums(songDTO) && this.albums.stream().allMatch(albumDTO -> songDTO.albums.stream().anyMatch(albumDTO::equalsWithoutSongs));
    }

    boolean equalsWithoutAlbums(SongDTO songDTO) {
        return id.equals(songDTO.id) && uuid.equals(songDTO.uuid) && title.equals(songDTO.title) && label.equals(songDTO.label) && releaseDate.equals(songDTO.releaseDate) && genre.equals(songDTO.genre) && musicbrainzId.equals(songDTO.musicbrainzId) && artists.equals(songDTO.artists) && Objects.equals(length, songDTO.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hashCodeWithoutAlbums(), this.albums.stream().map(AlbumDTO::hashCodeWithoutSongs).collect(Collectors.toSet()));
    }

    int hashCodeWithoutAlbums() {
        return Objects.hash(id, uuid, title, label, releaseDate, genre, musicbrainzId, artists, length);
    }

    public static class Builder {
        private final SongDTO instance;

        private Builder() {
            this.instance = new SongDTO();
        }

        public SongDTO.Builder withArticleSpecificData(
                Long id,
                UUID uuid,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                Set<ArtistDTO> artists
        ) {
            this.instance.id = id;
            this.instance.uuid = uuid;
            this.instance.title = title;
            this.instance.label = label;
            this.instance.releaseDate = releaseDate;
            this.instance.genre = genre;
            this.instance.musicbrainzId = musicbrainzId;
            this.instance.artists = artists;
            return this;
        }

        public SongDTO.Builder withSongSpecificData(
                Duration length
        ) {
            this.instance.length = length;
            return this;
        }

        public SongDTO.Builder withAlbums(
                Set<AlbumDTO> albums
        ) {
            this.instance.albums = albums;
            return this;
        }

        public SongDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in SongDTO");
            return this.instance;
        }
    }
}
