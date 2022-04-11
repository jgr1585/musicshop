package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.util.Objects;

public final class ArtistDTO implements Serializable {
    private static final long serialVersionUID = 5031759949516398235L;

    private Long id;

    private String name;

    public static ArtistDTO.Builder builder() {
        return new ArtistDTO.Builder();
    }

    public Long id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    private ArtistDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistDTO artistDTO = (ArtistDTO) o;
        return Objects.equals(id, artistDTO.id) && Objects.equals(name, artistDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class Builder {
        private ArtistDTO instance;

        private Builder() {
            this.instance = new ArtistDTO();
        }

        public ArtistDTO.Builder withArtistData(
                Long id,
                String name
        ) {
            this.instance.id = id;
            this.instance.name = name;
            return this;
        }

        public ArtistDTO build() {
            Objects.requireNonNull(this.instance.id, "id must be set in ArtistDTO");
            return this.instance;
        }
    }
}
