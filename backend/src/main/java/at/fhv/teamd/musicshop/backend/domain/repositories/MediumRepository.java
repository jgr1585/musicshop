package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.medium.Medium;

import java.util.Optional;
import java.util.Set;

public interface MediumRepository {
    Optional<Medium> findMediumById(Long id);

    // TODO: Remove "unnecessary" (?) repo method by using bidirectional entity mapping from article to medium
    Set<Medium> findMediumsByArticleId(Long id);

    void update(Medium medium);
}
