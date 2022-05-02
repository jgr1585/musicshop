package at.fhv.teamd.musicshop.backend.domain.topic;

import at.fhv.teamd.musicshop.backend.domain.DomainFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TopicTest {

    @Test
    void given_topicName_when_createTopic_then_topicName_equals() {
        // given
        String topicName = "Testname";

        // when
        Topic topic = DomainFactory.createTopic(topicName);

        // then
        Assertions.assertEquals(topicName, topic.getTopicName());
    }

    @Test
    void given_topic_when_compareTo_then_equals_or_doesNotEqual() {
        // given
        String topicName1 = "Testname1";
        String topicName2 = "Testname2";

        // when
        Topic topic1 = DomainFactory.createTopic(topicName1);
        Topic topic2 = DomainFactory.createTopic(topicName2);
        Topic topic3 = DomainFactory.createTopic(topicName1);

        // then
        Assertions.assertEquals(-1, topic1.compareTo(topic2));
        Assertions.assertEquals(1, topic2.compareTo(topic1));
        Assertions.assertEquals(0, topic1.compareTo(topic3));
    }
}