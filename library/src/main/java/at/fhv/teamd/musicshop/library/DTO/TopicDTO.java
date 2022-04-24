package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.util.Objects;

public class TopicDTO implements Serializable {
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

    public static class Builder {
        private TopicDTO instance;

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
