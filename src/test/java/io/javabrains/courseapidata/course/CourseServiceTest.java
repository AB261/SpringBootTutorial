package io.javabrains.courseapidata.course;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CourseServiceTest {

    @LocalServerPort
    int randomServerPort;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void getAllCourses() {
        courseService.getAllCourses("Java");
        verify(courseRepository, times(1)).findByTopicId("Java");
    }

    @Test
    void getCourse() {
        final Course course = new Course("Java", "Java Tools", "Java Tools tutorial", "SpringBoot");
        courseService.getCourse(course.getId());
        verify(courseRepository, times(1)).findById(course.getId());
    }

    @Test
    void addCourse() {
        final Course course = new Course("Java", "Java Tools", "Java Tools tutorial", "SpringBoot");
        courseService.addCourse(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void updateCourse() {
        final Course course = new Course("Java", "Java Tools", "Java Tools tutorial", "SpringBoot");
        courseService.updateCourse(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void deleteCourse() {
        final Course course = new Course("Java", "Java Tools", "Java Tools tutorial", "SpringBoot");
        doNothing().when(courseRepository).deleteById(anyString());
        courseService.deleteCourse(course.getId());
        verify(courseRepository, times(1)).deleteById(course.getId());
    }
}