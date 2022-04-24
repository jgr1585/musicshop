package at.fhv.teamd.musicshop.backend.domain.repositories;

import at.fhv.teamd.musicshop.backend.domain.topic.Topic;

import java.util.Set;

public interface TopicRepository {
    Set<Topic> findAllTopics();
}
