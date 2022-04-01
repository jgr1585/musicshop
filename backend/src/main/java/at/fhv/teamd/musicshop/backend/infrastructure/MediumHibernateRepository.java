package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.medium.AnalogMedium;
import at.fhv.teamd.musicshop.backend.domain.repositories.MediumRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

public class MediumHibernateRepository implements MediumRepository {

    // package-private constructor to enable initialization only through same package classes
    MediumHibernateRepository() {}

    @Override
    @Transactional
    public Optional<AnalogMedium> findAnalogMediumById(Long id) {
        EntityManager em = PersistenceManager.getEntityManagerInstance();

        TypedQuery<AnalogMedium> query = em.createQuery("SELECT m FROM AnalogMedium m " +
                "WHERE m.id=:id", AnalogMedium.class);

        query.setParameter("id", id);

        Optional<AnalogMedium> analogMediumOpt;

        try {
            analogMediumOpt= Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            analogMediumOpt= Optional.empty();
        }

        em.close();

        return analogMediumOpt;
    }
}
