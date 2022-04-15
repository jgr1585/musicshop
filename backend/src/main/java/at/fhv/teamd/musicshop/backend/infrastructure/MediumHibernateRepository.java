package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.medium.Medium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MediumHibernateRepository implements MediumRepository {

    // package-private constructor to enable initialization only through same package classes
    MediumHibernateRepository() {
    }

    @Override
    @Transactional
    public Optional<Medium> findMediumById(Long id) {
        Objects.requireNonNull(id);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<Medium> query = em.createQuery("SELECT m FROM Medium m " +
                "WHERE m.id=:id", Medium.class);

        query.setParameter("id", id);

        Optional<Medium> mediumOpt;

        try {
            mediumOpt = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            mediumOpt = Optional.empty();
        }

        em.close();

        return mediumOpt;
    }

    @Override
    public void update(Medium medium) {
        Objects.requireNonNull(medium);

        EntityManager em = PersistenceManager.getEntityManagerInstance();

        em.getTransaction().begin();

        em.merge(medium);

        em.getTransaction().commit();

        em.close();
    }
}
