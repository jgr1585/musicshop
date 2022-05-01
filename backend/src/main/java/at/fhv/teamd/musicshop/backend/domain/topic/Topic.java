package at.fhv.teamd.musicshop.backend.domain.topic;

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
    private UUID uuid;

    protected Topic() {
    }

    public Topic(String name) {
        this.uuid = UUID.randomUUID();
        Objects.requireNonNull(this.name = name);
    }

    @Override
    public String getTopicName() {
        return this.name;
    }

    @Override
    public int compareTo(Topic o) {
        return name.compareTo(o.getTopicName());
    }
}
