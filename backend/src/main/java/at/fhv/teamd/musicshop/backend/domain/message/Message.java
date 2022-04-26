package at.fhv.teamd.musicshop.backend.domain.message;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Message {
    
    private final String topicName;
    private final UUID uuid;
    private final String title;
    private final String body;

    private Message(String topicName, String title, String body) {
        this.uuid = UUID.randomUUID();
        this.topicName = Objects.requireNonNull(topicName);
        this.title = Objects.requireNonNull(title);
        this.body = Objects.requireNonNull(body);
    }

    public static Message of(String topicName, String title, String body) {
        return new Message(topicName, title, body);
    }
}
