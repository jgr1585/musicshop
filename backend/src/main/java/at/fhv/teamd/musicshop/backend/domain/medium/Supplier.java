package at.fhv.teamd.musicshop.backend.domain.medium;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Objects;

@Entity
public class Supplier {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;
    @Column
    private Duration supplyDuration;

    protected Supplier() {}

    public Supplier(String name, Duration supplyDuration) {
        this.name = Objects.requireNonNull(name);
        this.supplyDuration = Objects.requireNonNull(supplyDuration);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Duration getSupplyDuration() {
        return supplyDuration;
    }
}
