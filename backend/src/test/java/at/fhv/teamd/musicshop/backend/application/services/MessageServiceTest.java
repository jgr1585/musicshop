package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import at.fhv.teamd.musicshop.backend.domain.repositories.TopicRepository;
import at.fhv.teamd.musicshop.backend.domain.topic.Topic;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.dto.TopicDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    private final TopicRepository topicRepository;

    private final MessageService messageService;

    private MessageServiceTest() {
        this.topicRepository = RepositoryFactory.getTopicRepositoryInstance();
        this.messageService = new MessageService();
    }

    @Test
    void getAllTopics() {
        // given
        Set<Topic> topics = Set.of(DomainFactory.createTopic(), DomainFactory.createTopic(), DomainFactory.createTopic());

        Mockito.when(topicRepository.findAllTopics()).thenReturn(topics);

        Set<TopicDTO> expectedTopics = topics.stream()
                .map(DTOProvider::buildTopicDTO)
                .collect(Collectors.toSet());

        // when
        Set<TopicDTO> actualTopics = messageService.getAllTopics();

        // then
        Assertions.assertEquals(expectedTopics, actualTopics);
    }
}