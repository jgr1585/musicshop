package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;

import java.util.Optional;

public interface MediumRepository {
    Optional<AnalogMedium> findAnalogMediumById(Long id);
}
