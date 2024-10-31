package upc.edu.pe.BrightMind.lesson_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.LearningPath;
import upc.edu.pe.BrightMind.lesson_service.model.entities.Lesson;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.User;
import upc.edu.pe.BrightMind.lesson_service.repository.LearningPathRepository;
import upc.edu.pe.BrightMind.lesson_service.repository.LessonRepository;
import upc.edu.pe.BrightMind.lesson_service.repository.UserRepository;
import upc.edu.pe.BrightMind.lesson_service.service.LearningPathService;
import upc.edu.pe.BrightMind.lesson_service.service.util.ResourceNotFoundException;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LearningPathServiceTest {

    @Mock
    private LearningPathRepository learningPathRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LearningPathService learningPathService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLearningPath() {
        CreateLearningPathDTO dto = new CreateLearningPathDTO();
        dto.setName("Path 1");
        dto.setUserId(1L);
        dto.setLessonIds(Arrays.asList(1L, 2L));

        User user = new User();
        user.setId(1L);

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(lessonRepository.findAllById(dto.getLessonIds())).thenReturn(Arrays.asList(lesson1, lesson2));

        LearningPath savedPath = new LearningPath("Path 1", user);
        when(learningPathRepository.save(any(LearningPath.class))).thenReturn(savedPath);

        LearningPathDTO result = learningPathService.createLearningPath(dto);
        assertThat(result.getName()).isEqualTo("Path 1");
        verify(learningPathRepository, times(1)).save(any(LearningPath.class));
    }

    @Test
    public void testGetLearningPathById_NotFound() {
        when(learningPathRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> learningPathService.getLearningPathById(1L));
    }

    @Test
    public void testGetAllLearningPaths() {
        LearningPath path1 = new LearningPath("Path 1", new User());
        LearningPath path2 = new LearningPath("Path 2", new User());
        when(learningPathRepository.findAll()).thenReturn(Arrays.asList(path1, path2));

        List<LearningPathDTO> result = learningPathService.getAllLearningPaths();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testDeleteLearningPath_NotFound() {
        when(learningPathRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> learningPathService.deleteLearningPath(1L));
    }
}

