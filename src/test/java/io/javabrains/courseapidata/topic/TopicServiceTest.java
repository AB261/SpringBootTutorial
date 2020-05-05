package io.javabrains.courseapidata.topic;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TopicServiceTest {

    @LocalServerPort
    int randomServerPort;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;


    @Test
    void getAllTopics() {
        topicService.getAllTopics();
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    void getTopic() {
        final Topic topic = new Topic("Java", "Java 8", "Java 8 tutorial");
        topicService.getTopic(topic.getId());
        verify(topicRepository, times(1)).findById(topic.getId());
    }

    @Test
    void addTopic() {
        final Topic topic = new Topic("Java", "Java 8", "Java 8 tutorial");
        topicService.addTopic(topic);
        verify(topicRepository, times(1)).save(topic);

    }

    @Test
    void updateTopic() {
        final Topic topic = new Topic("Java", "Java 8", "Java 8 tutorial");
        topicService.updateTopic(topic.getId(), topic);
        verify(topicRepository, times(1)).save(topic);
    }

    @Test
    void deleteTopic() {
        final Topic topic = new Topic("Java", "Java 8", "Java 8 tutorial");
        doNothing().when(topicRepository).deleteById(anyString());
        topicService.deleteTopic(topic.getId());
        verify(topicRepository, times(1)).deleteById(topic.getId());

    }
}