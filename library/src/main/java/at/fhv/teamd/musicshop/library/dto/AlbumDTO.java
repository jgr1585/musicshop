package at.fhv.teamd.musicshop.library.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public final class AlbumDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = 2304687831949112622L;

    private Long id;
    private UUID uuid;
    private String title;
    private String label;
    private LocalDate releaseDate;
    private String genre;
    private String musicbrainzId;
    private List<ArtistDTO> artists;
    private List<MediumDTO> mediums;
    private List<SongDTO> songs;

    public static AlbumDTO.Builder builder() {
        return new AlbumDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

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

    public String musicbrainzId() {
        return this.musicbrainzId;
    }

    public List<ArtistDTO> artists() {
        return Collections.unmodifiableList(artists);
    }

    @JsonProperty(required = true)
    public List<MediumDTO> mediums() {
        return Collections.unmodifiableList(mediums);
    }

    @JsonProperty(required = true)
    public List<SongDTO> songs() {
        return Collections.unmodifiableList(songs);
    }

    private AlbumDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumDTO albumDTO = (AlbumDTO) o;
        return this.equalsWithoutSongs(albumDTO) && this.songs.stream().allMatch(songDTO -> albumDTO.songs.stream().anyMatch(songDTO::equalsWithoutAlbums));
    }

    boolean equalsWithoutSongs(AlbumDTO albumDTO) {
        return id.equals(albumDTO.id) && uuid.equals(albumDTO.uuid) && title.equals(albumDTO.title) && label.equals(albumDTO.label) && releaseDate.equals(albumDTO.releaseDate) && genre.equals(albumDTO.genre) && musicbrainzId.equals(albumDTO.musicbrainzId) && artists.equals(albumDTO.artists) && Objects.equals(mediums, albumDTO.mediums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hashCodeWithoutSongs(), this.songs.stream().map(SongDTO::hashCodeWithoutAlbums).collect(Collectors.toSet()));
    }

    int hashCodeWithoutSongs() {
        return Objects.hash(id, uuid, title, label, releaseDate, genre, musicbrainzId, artists, mediums);
    }

    public static class Builder {
        private final AlbumDTO instance;

        private Builder() {
            this.instance = new AlbumDTO();
        }

        public AlbumDTO.Builder withArticleSpecificData(
                Long id,
                UUID uuid,
                String title,
                String label,
                LocalDate releaseDate,
                String genre,
                String musicbrainzId,
                List<ArtistDTO> artists
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

        public AlbumDTO.Builder withAlbumSpecificData(
                List<MediumDTO> mediums
        ) {
            this.instance.mediums = mediums;
            return this;
        }

        public AlbumDTO.Builder withSongs(
                List<SongDTO> songs
        ) {
            this.instance.songs = songs;
            return this;
        }

        public AlbumDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in AlbumDTO");
            return this.instance;
        }
    }
}
