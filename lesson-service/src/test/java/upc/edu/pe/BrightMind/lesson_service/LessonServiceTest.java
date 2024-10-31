package upc.edu.pe.BrightMind.lesson_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.Lesson;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;
import upc.edu.pe.BrightMind.lesson_service.repository.LessonRepository;
import upc.edu.pe.BrightMind.lesson_service.service.LessonService;
import upc.edu.pe.BrightMind.lesson_service.service.util.ResourceNotFoundException;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLesson() {
        CreateLessonDTO dto = new CreateLessonDTO();
        dto.setTitle("Lesson 1");
        dto.setSubject(Subject.MATH);
        dto.setGrade(Grade.FIRST_YEAR);
        dto.setContent("Math content");

        Lesson lesson = new Lesson("Lesson 1", Subject.MATH, Grade.FIRST_YEAR, "Math content");
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonDTO result = lessonService.createLesson(dto);
        assertThat(result.getTitle()).isEqualTo("Lesson 1");
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testGetLessonById_NotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lessonService.getLessonById(1L));
    }

    @Test
    public void testGetAllLessons() {
        Lesson lesson1 = new Lesson("Lesson 1", Subject.MATH, Grade.FIRST_YEAR, "Content 1");
        Lesson lesson2 = new Lesson("Lesson 2", Subject.SCIENCE, Grade.SECOND_YEAR, "Content 2");

        when(lessonRepository.findAll()).thenReturn(Arrays.asList(lesson1, lesson2));

        List<LessonDTO> result = lessonService.getAllLessons();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testDeleteLesson_NotFound() {
        when(lessonRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> lessonService.deleteLesson(1L));
    }
}

