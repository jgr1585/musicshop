package at.fhv.teamd.musicshop.backend.domain.topic;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Entity
public class Topic {
    @Id
    private String name;

    protected Topic() {
    }

    public Topic(String name) {
        Objects.requireNonNull(this.name = name);
    }
}
