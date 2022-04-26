package at.fhv.teamd.musicshop.backend.domain.message;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message {
    private final UUID uuid;
    private final String topicName;
    private final String title;
    private final String body;
    private final Instant sentOnTimestamp;

    private Message(String topicName, String title, String body, Instant sentOnTimestamp) {
        this.uuid = UUID.randomUUID();
        this.topicName = Objects.requireNonNull(topicName);
        this.title = Objects.requireNonNull(title);
        this.body = Objects.requireNonNull(body);
        this.sentOnTimestamp = sentOnTimestamp;
    }

    public static Message of(String topicName, String title, String body) {
        return new Message(topicName, title, body, null);
    }

    public static Message of(String topicName, String title, String body, Instant sentOnTimestamp) {
        return new Message(topicName, title, body, sentOnTimestamp);
    }
}
