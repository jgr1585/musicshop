package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;

import java.util.Optional;

public interface MediumRepository {
    Optional<Medium> findMediumById(Long id);
    void update(Medium medium);
}
