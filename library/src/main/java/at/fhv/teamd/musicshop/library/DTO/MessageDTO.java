package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public final class MessageDTO implements Serializable {

    private String topic;
    private String title;
    private String body;

    public static MessageDTO.Builder builder() {
        return new MessageDTO.Builder();
    }

    public String topic() {
        return this.topic;
    }

    public String title() {
        return this.title;
    }

    public String body() {
        return this.body;
    }

    private MessageDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(topic, that.topic) && Objects.equals(title, that.title) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, title, body);
    }

    public static class Builder {
        private final MessageDTO instance;

        private Builder() {
            this.instance = new MessageDTO();
        }

        public MessageDTO.Builder withMessageData(
                String topic,
                String title,
                String body
        ) {
            this.instance.topic = topic;
            this.instance.title = title;
            this.instance.body = body;
            return this;
        }

        public MessageDTO build() {
            Objects.requireNonNull(this.instance.topic, "topic must be set in MessageDTO");
            Objects.requireNonNull(this.instance.title, "title must be set in MessageDTO");
            Objects.requireNonNull(this.instance.body, "body must be set in MessageDTO");
            return this.instance;
        }
    }
}
