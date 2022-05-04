package at.fhv.teamd.musicshop.backend.domain.message;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message {
    private final String id;
    private final String topicName;
    private final String title;
    private final String body;
    private final Instant sentOnTimestamp;

    private Message(String id, String topicName, String title, String body, Instant sentOnTimestamp) {
        this.id = Objects.requireNonNull(id);
        this.topicName = Objects.requireNonNull(topicName);
        this.title = Objects.requireNonNull(title);
        this.body = Objects.requireNonNull(body);
        this.sentOnTimestamp = sentOnTimestamp;
    }

    public static Message of(String topicName, String title, String body) {
        return new Message(UUID.randomUUID().toString(), topicName, title, body, null);
    }

    public static Message of(String topicName, String title, String body, Instant sentOnTimestamp) {
        return new Message(UUID.randomUUID().toString(), topicName, title, body, sentOnTimestamp);
    }

    public static Message of(String id, String topicName, String title, String body, Instant sentOnTimestamp) {
        return new Message(id, topicName, title, body, sentOnTimestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
