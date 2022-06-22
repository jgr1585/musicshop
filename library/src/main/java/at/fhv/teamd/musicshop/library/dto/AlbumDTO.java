package at.fhv.teamd.musicshop.library.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class AlbumDTO implements ArticleDTO, Serializable {
    private static final long serialVersionUID = -3423050844004290443L;

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
        return this.equalsWithoutSongs(albumDTO) && this.songs.stream().allMatch(songDTO -> albumDTO.songs.stream().anyMatch(songDTO::equalsWithoutAlbums));
    }

    boolean equalsWithoutSongs(AlbumDTO albumDTO) {
        return id.equals(albumDTO.id) && title.equals(albumDTO.title) && label.equals(albumDTO.label) && releaseDate.equals(albumDTO.releaseDate) && genre.equals(albumDTO.genre) && musicbrainzId.equals(albumDTO.musicbrainzId) && artists.equals(albumDTO.artists) && Objects.equals(mediums, albumDTO.mediums);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hashCodeWithoutSongs(), this.songs.stream().map(SongDTO::hashCodeWithoutAlbums).collect(Collectors.toSet()));
    }

    int hashCodeWithoutSongs() {
        return Objects.hash(id, title, label, releaseDate, genre, musicbrainzId, artists, mediums);
    }

    public static class Builder {
        private final AlbumDTO instance;

        private Builder() {
            this.instance = new AlbumDTO();
        }

        public AlbumDTO.Builder withArticleSpecificData(
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

        public AlbumDTO.Builder withAlbumSpecificData(
                Set<MediumDTO> mediums
        ) {
            this.instance.mediums = mediums;
            return this;
        }

        public AlbumDTO.Builder withSongs(
                Set<SongDTO> songs
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
