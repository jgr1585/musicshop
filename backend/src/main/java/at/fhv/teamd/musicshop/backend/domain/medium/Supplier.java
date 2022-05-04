package at.fhv.teamd.musicshop.backend.domain.medium;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Supplier {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private UUID uuid;

    @Column
    private String name;

    @Column
    private Duration supplyDuration;

    protected Supplier() {}

    public Supplier(String name, Duration supplyDuration) {
        this.uuid = UUID.randomUUID();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return uuid.equals(supplier.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
