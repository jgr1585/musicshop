package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.application.PersistenceManager;
import at.fhv.teamd.musicshop.backend.domain.repositories.TopicRepository;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

public class TopicHibernateRepository implements TopicRepository {

    @Override
    @Transactional
    public Set<Topic> findAllTopics() {
        EntityManager em = PersistenceManager.getEntityManagerInstance();

        Set<Topic> topics = em.createQuery(
                "SELECT t FROM Topic t", Topic.class)
                .getResultList()
                .stream()
                .collect(Collectors.toUnmodifiableSet());

        em.close();
        return topics;
    }

}
