package io.javabrains.courseapidata.course;

import io.javabrains.courseapidata.topic.Topic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest {
    Course course = new Course("Java Tools", "Java Tools Version 2.0", "Cool new tutorial", "Java Spring Boot");

    @Test
    void getName() {
        assertEquals(course.getName(), "Java Tools Version 2.0");
    }

    @Test
    void setName() {
        course.setName("Java Tools Version 2.0 Updated");
        assertEquals(course.getName(), "Java Tools Version 2.0 Updated");
    }

    @Test
    void getId() {
        assertEquals(course.getId(), "Java Tools");
    }

    @Test
    void setId() {
        course.setId("Java Tools Updated");
        assertEquals(course.getId(), "Java Tools Updated");
    }

    @Test
    void getDescription() {
        assertEquals(course.getDescription(), "Cool new tutorial");
    }

    @Test
    void setDescription() {
        course.setDescription("Even cooler tutorial");
        assertEquals(course.getDescription(), "Even cooler tutorial");
    }

    @Test
    void getTopic() {
        Topic topic = course.getTopic();
        assertEquals(topic.getId(), "Java Spring Boot");
    }

    @Test
    void setTopic() {
        Topic topic = new Topic("Java Spring Boot Updated", " ", " ");
        course.setTopic(topic);
        assertEquals(topic.getId(), "Java Spring Boot Updated");
    }
}