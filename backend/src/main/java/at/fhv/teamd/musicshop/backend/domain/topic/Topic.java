package at.fhv.teamd.musicshop.backend.domain.topic;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Topic implements javax.jms.Topic, Comparable<Topic> {
    @Id
    private String name;

    @Column(unique = true)
    @Getter private UUID uuid;

    protected Topic() {
    }

    public Topic(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
    }

    @Override
    public String getTopicName() {
        return this.name;
    }

    @Override
    public int compareTo(Topic o) {
        return name.compareTo(o.getTopicName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return uuid.equals(topic.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
