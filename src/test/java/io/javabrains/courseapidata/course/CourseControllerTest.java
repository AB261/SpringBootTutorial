package io.javabrains.courseapidata.course;

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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CourseControllerTest {

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    int randomServerPort;
    @MockBean
    private CourseService courseService;
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
    void getAllCourses() throws Exception {
        when(courseService.getAllCourses("JavaSpring")).thenReturn(new ArrayList<>
                (Arrays.asList(new Course("JavaSpring", "Java spring tutorial", "cool tutorial", "Java Spring Boot"))));


        final String baseUri = "http://localhost:" + randomServerPort + "/topics/JavaSpring/courses";
        URI uri = new URI(baseUri);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());

        this.mockMvc.perform(get("/topics/JavaSpring/courses").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("JavaSpring")))
                .andExpect(jsonPath("$[0].name", is("Java spring tutorial")))
                .andExpect(jsonPath("$[0].description", is("cool tutorial")));
    }

    @Test
    void getCourse() throws Exception {
        Course course = new Course("Java", "Java tutorial for beginners", "Interesting tutorial", "JavaSpring");

        when(courseService.getCourse("Java")).thenReturn(Optional.of(course));

        this.mockMvc.perform(get("/topics/JavaSpring/courses/Java").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is("Java")))
                .andExpect(jsonPath("name", is("Java tutorial for beginners")))
                .andExpect(jsonPath("description", is("Interesting tutorial")));

    }

    @Test
    void addCourse() throws Exception {
        Course course = new Course("Java", "Java tutorial for beginners", "Interesting tutorial", "JavaSpring");

        when(courseService.addCourse(course)).thenReturn(course);

        this.mockMvc.perform(post("/topics/JavaSpring/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(course)))
                .andExpect(status().isOk());


        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService, times(1)).addCourse(courseArgumentCaptor.capture());

        Course courseArg = courseArgumentCaptor.getValue();
        assertEquals(courseArg.getId(), course.getId());
        assertEquals(courseArg.getDescription(), course.getDescription());
        assertEquals(courseArg.getName(), course.getName());
    }

    @Test
    void updateCourse() throws Exception {
        Course course = new Course("Java", "Java tutorial for beginners", "Interesting tutorial", "JavaSpring");

        when(courseService.updateCourse(course)).thenReturn(course);

        this.mockMvc.perform(put("/topics/JavaSpring/courses/Java")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(course)))
                .andExpect(status().isOk());


        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService, times(1)).updateCourse(courseArgumentCaptor.capture());

        Course courseArg = courseArgumentCaptor.getValue();
        assertEquals(courseArg.getId(), course.getId());
        assertEquals(courseArg.getDescription(), course.getDescription());
        assertEquals(courseArg.getName(), course.getName());
    }

    @Test
    void deleteCourse() throws Exception {
        this.mockMvc.perform(delete("/topics/JavaSpring/courses/Java")).andExpect(status().isOk());
    }
}