package at.fhv.teamd.musicshop.backend.domain.message;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class Message {
    private String topicName;

    private String title;

    private String body;

    private Instant sentOnTimestamp;

    private Message(String topicName, String title, String body, Instant sentOnTimestamp) {
        this.topicName = Objects.requireNonNull(topicName);
        this.title = Objects.requireNonNull(title);
        this.body = Objects.requireNonNull(body);
    }

    public static Message of(String topicName, String title, String body) {
        return new Message(topicName, title, body, null);
    }

    public static Message of(String topicName, String title, String body, Instant sentOnTimestamp) {
        return new Message(topicName, title, body, sentOnTimestamp);
    }
}
