package io.javabrains.courseapidata.topic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicTest {

    Topic topic = new Topic("Java", "Java Spring Boot", "Cool new topic to test");

    @Test
    void getName() {
        assertEquals(topic.getName(), "Java Spring Boot");
    }

    @Test
    void setName() {
        topic.setName("Java Spring Boot 2");
        assertEquals(topic.getName(), "Java Spring Boot 2");
    }

    @Test
    void getId() {
        assertEquals(topic.getId(), "Java");
    }

    @Test
    void setId() {
        topic.setId("Java 2");
        assertEquals(topic.getId(), "Java 2");
    }

    @Test
    void getDescription() {
        assertEquals(topic.getDescription(), "Cool new topic to test");
    }

    @Test
    void setDescription() {
        topic.setDescription("Another Topic to test");
        assertEquals(topic.getDescription(), "Another Topic to test");
    }
}