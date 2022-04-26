package at.fhv.teamd.musicshop.library.DTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public final class MessageDTO implements Serializable {
    private static final long serialVersionUID = -135325125784608818L;

    private TopicDTO topic;

    private String uuid;

    private String title;

    private String body;

    private Instant sentOnTimestamp;

    public static MessageDTO.Builder builder() {
        return new MessageDTO.Builder();
    }

    public TopicDTO topic() {
        return this.topic;
    }

    public String uuid() {
        return this.uuid;
    }

    public String title() {
        return this.title;
    }

    public String body() {
        return this.body;
    }

    public Instant sentOnTimestamp() { return this.sentOnTimestamp; }

    private MessageDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return topic.equals(that.topic) && uuid.equals(that.uuid) && title.equals(that.title) && body.equals(that.body) && Objects.equals(sentOnTimestamp, that.sentOnTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, uuid, title, body, sentOnTimestamp);
    }

    public static class Builder {
        private final MessageDTO instance;

        private Builder() {
            this.instance = new MessageDTO();
        }

        public MessageDTO.Builder withMessageData(
                TopicDTO topic,
                String uuid,
                String title,
                String body
        ) {
            this.instance.topic = topic;
            this.instance.uuid = uuid;
            this.instance.title = title;
            this.instance.body = body;
            return this;
        }

        public MessageDTO.Builder withMessageSentOnTimestamp(Instant sentOnTimestamp) {
            this.instance.sentOnTimestamp = sentOnTimestamp;
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
