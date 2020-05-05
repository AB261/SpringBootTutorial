package io.javabrains.courseapidata.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TopicControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    int randomServerPort;
    @MockBean
    private TopicService topicService;
    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllTopics() throws Exception {


        when(topicService.getAllTopics()).thenReturn(new ArrayList<>
                (Arrays.asList(new Topic("Java Spring", "Java spring tutorial", "cool tutorial"))));


        final String baseUri = "http://localhost:" + randomServerPort + "/topics";
        URI uri = new URI(baseUri);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());

        this.mockMvc.perform(get("/topics").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("Java Spring")))
                .andExpect(jsonPath("$[0].name", is("Java spring tutorial")))
                .andExpect(jsonPath("$[0].description", is("cool tutorial")));

    }

    @Test
    void getTopic() throws Exception {
        Topic topic = new Topic("Java", "Java tutorial for beginners", "Interesting tutorial");

        when(topicService.getTopic("Java")).thenReturn(Optional.of(topic));

        this.mockMvc.perform(get("/topics/Java").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is("Java")))
                .andExpect(jsonPath("name", is("Java tutorial for beginners")))
                .andExpect(jsonPath("description", is("Interesting tutorial")));

    }

    @Test
    void addTopic() throws Exception {
        Topic topic = new Topic("Java", "Java tutorial for beginners", "Interesting tutorial");

        when(topicService.addTopic(any(Topic.class))).thenReturn(topic);

        this.mockMvc.perform(post("/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topic)))
                .andExpect(status().isOk());


        ArgumentCaptor<Topic> topicArgumentCaptor = ArgumentCaptor.forClass(Topic.class);
        verify(topicService, times(1)).addTopic(topicArgumentCaptor.capture());

        Topic topicArg = topicArgumentCaptor.getValue();
        assertEquals(topicArg.getId(), topic.getId());
        assertEquals(topicArg.getDescription(), topic.getDescription());
        assertEquals(topicArg.getName(), topic.getName());

    }

    @Test
    void updateTopic() throws Exception {

        Topic topic = new Topic("Java", "Java tutorial for beginners", "Interesting tutorial");

        when(topicService.updateTopic(anyString(), any(Topic.class))).thenReturn(topic);

        this.mockMvc.perform(put("/topics/Java")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topic)))
                .andExpect(status().isOk());

        ArgumentCaptor<Topic> topicArgumentCaptor = ArgumentCaptor.forClass(Topic.class);
        ArgumentCaptor<String> StringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(topicService, times(1)).updateTopic(StringArgumentCaptor.capture(), topicArgumentCaptor.capture());

        Topic topicArg = topicArgumentCaptor.getValue();
        assertEquals(topicArg.getId(), topic.getId());
        assertEquals(topicArg.getDescription(), topic.getDescription());
        assertEquals(topicArg.getName(), topic.getName());

        String idArg = StringArgumentCaptor.getValue();
        assertEquals(idArg, topic.getId());
    }

    @Test
    void deleteTopic() throws Exception {
        this.mockMvc.perform(delete("/topics/Java")).andExpect(status().isOk());
    }
}