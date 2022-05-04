package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.util.Objects;

public final class TopicDTO implements Serializable {
    private static final long serialVersionUID = -4281216794249781470L;

    private String name;

    public static TopicDTO.Builder builder() {
        return new TopicDTO.Builder();
    }

    public String name() {
        return this.name;
    }

    private TopicDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicDTO topicDTO = (TopicDTO) o;
        return Objects.equals(name, topicDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {
        private final TopicDTO instance;

        private Builder() {
            this.instance = new TopicDTO();
        }

        public TopicDTO.Builder withTopicData(
                String name
        ) {
            this.instance.name = name;
            return this;
        }

        public TopicDTO build() {
            Objects.requireNonNull(this.instance.name, "name must be set in ArtistDTO");
            return this.instance;
        }
    }
}
