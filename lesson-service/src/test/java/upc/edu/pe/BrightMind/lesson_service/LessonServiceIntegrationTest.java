package upc.edu.pe.BrightMind.lesson_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;
import upc.edu.pe.BrightMind.lesson_service.repository.LearningPathRepository;
import upc.edu.pe.BrightMind.lesson_service.repository.LessonRepository;
import upc.edu.pe.BrightMind.lesson_service.service.LessonService;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LessonServiceIntegrationTest {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LearningPathRepository learningPathRepository;
    @BeforeEach
    public void setup() {
        // Primero elimina los Learning Paths para evitar la referencia a las lecciones
        learningPathRepository.deleteAll();
        lessonRepository.deleteAll();
    }
    @Test
    public void testCreateLesson() {
        // Cubre la historia de usuario: US017 - Acceder a una Biblioteca de Recursos Educativos
        // Permite crear una lección que será parte de la biblioteca de recursos educativos.
        CreateLessonDTO dto = new CreateLessonDTO();
        dto.setTitle("Lesson 1");
        dto.setSubject(Subject.MATH);
        dto.setGrade(Grade.FIRST_YEAR);
        dto.setContent("Math content");

        LessonDTO result = lessonService.createLesson(dto);

        assertThat(result.getTitle()).isEqualTo("Lesson 1");
        assertThat(result.getSubject()).isEqualTo(Subject.MATH);
        assertThat(result.getGrade()).isEqualTo(Grade.FIRST_YEAR);
    }

    @Test
    public void testGetLessonsBySubject() {
        // Crear lección con asignatura MATH
        CreateLessonDTO mathLessonDTO = new CreateLessonDTO();
        mathLessonDTO.setTitle("Math Lesson");
        mathLessonDTO.setSubject(Subject.MATH);
        mathLessonDTO.setGrade(Grade.FIRST_YEAR);
        mathLessonDTO.setContent("Math content");
        lessonService.createLesson(mathLessonDTO);

        // Crear otra lección con una asignatura diferente para verificar el filtrado
        CreateLessonDTO scienceLessonDTO = new CreateLessonDTO();
        scienceLessonDTO.setTitle("Science Lesson");
        scienceLessonDTO.setSubject(Subject.SCIENCE);
        scienceLessonDTO.setGrade(Grade.FIRST_YEAR);
        scienceLessonDTO.setContent("Science content");
        lessonService.createLesson(scienceLessonDTO);

        List<LessonDTO> lessons = lessonService.getLessonsBySubject(Subject.MATH);

        // Verificación
        assertThat(lessons.size()).isEqualTo(1);
        assertThat(lessons.get(0).getTitle()).isEqualTo("Math Lesson");
        assertThat(lessons.get(0).getSubject()).isEqualTo(Subject.MATH);
    }


    @Test
    public void testGetLessonsByGrade() {
        // Crear lección con grado SECOND_YEAR
        CreateLessonDTO scienceLessonDTO = new CreateLessonDTO();
        scienceLessonDTO.setTitle("Science Lesson");
        scienceLessonDTO.setSubject(Subject.SCIENCE);
        scienceLessonDTO.setGrade(Grade.SECOND_YEAR);
        scienceLessonDTO.setContent("Science content");
        lessonService.createLesson(scienceLessonDTO);

        // Crear otra lección con un grado diferente para verificar el filtrado
        CreateLessonDTO mathLessonDTO = new CreateLessonDTO();
        mathLessonDTO.setTitle("Math Lesson");
        mathLessonDTO.setSubject(Subject.MATH);
        mathLessonDTO.setGrade(Grade.FIRST_YEAR);
        mathLessonDTO.setContent("Math content");
        lessonService.createLesson(mathLessonDTO);

        List<LessonDTO> lessons = lessonService.getLessonsByGrade(Grade.SECOND_YEAR);

        // Verificación
        assertThat(lessons.size()).isEqualTo(1);
        assertThat(lessons.get(0).getTitle()).isEqualTo("Science Lesson");
        assertThat(lessons.get(0).getGrade()).isEqualTo(Grade.SECOND_YEAR);
    }


    @Test
    public void testCreateAndDeleteLesson() {
        // Crear una lección
        CreateLessonDTO dto = new CreateLessonDTO();
        dto.setTitle("Math Lesson");
        dto.setSubject(Subject.MATH);
        dto.setGrade(Grade.FIRST_YEAR);
        dto.setContent("This is a math lesson");

        LessonDTO createdLesson = lessonService.createLesson(dto);
        assertThat(createdLesson).isNotNull();
        assertThat(createdLesson.getTitle()).isEqualTo("Math Lesson");

        // Verifica que puedes eliminar la lección sin restricciones de integridad
        lessonRepository.deleteById(createdLesson.getId());
        assertThat(lessonRepository.existsById(createdLesson.getId())).isFalse();
    }
}

