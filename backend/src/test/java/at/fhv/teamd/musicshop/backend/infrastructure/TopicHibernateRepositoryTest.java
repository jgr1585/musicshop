package at.fhv.teamd.musicshop.backend.infrastructure;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TopicHibernateRepositoryTest {
    private TopicHibernateRepository topicHibernateRepository;

    @BeforeEach
    void init() {
        topicHibernateRepository = new TopicHibernateRepository();
    }

    @Test
    void given_topicRepository_when_getAllTopics_returnSetOfTopics() {
        //given
        Set<Topic> expectedTopics = BaseRepositoryData.getTopics();

        //when
        Set<Topic> actualTopics = this.topicHibernateRepository.findAllTopics();

        //then
        Assertions.assertTrue(expectedTopics.containsAll(actualTopics));
        Assertions.assertTrue(actualTopics.containsAll(expectedTopics));
    }
}