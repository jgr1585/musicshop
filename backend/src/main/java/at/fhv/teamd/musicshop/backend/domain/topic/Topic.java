package at.fhv.teamd.musicshop.backend.domain.topic;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Topic {
    @Id
    private String name;

    protected Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }
}
