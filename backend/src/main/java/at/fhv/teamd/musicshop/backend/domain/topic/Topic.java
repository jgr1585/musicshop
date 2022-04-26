package at.fhv.teamd.musicshop.backend.domain.topic;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Topic implements javax.jms.Topic {
    @Id
    private String name;

    protected Topic() {
    }

    public Topic(String name) {
        Objects.requireNonNull(this.name = name);
    }

    @Override
    public String getTopicName() {
        return this.name;
    }
}
