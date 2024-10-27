package upc.edu.pe.BrightMind.lesson_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.Lesson;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;
import upc.edu.pe.BrightMind.lesson_service.repository.LessonRepository;
import upc.edu.pe.BrightMind.lesson_service.service.util.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public LessonDTO createLesson(CreateLessonDTO dto) {
        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());
        lesson.setSubject(dto.getSubject());
        lesson.setGrade(dto.getGrade());
        lesson.setContent(dto.getContent());

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToDTO(savedLesson);
    }

    @Transactional
    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        return mapToDTO(lesson);
    }

    @Transactional
    public List<LessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessons.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<LessonDTO> getLessonsBySubject(Subject subject) {
        List<Lesson> lessons = lessonRepository.findBySubject(subject);
        return lessons.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<LessonDTO> getLessonsByGrade(Grade grade) {
        List<Lesson> lessons = lessonRepository.findByGrade(grade);
        return lessons.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public LessonDTO updateLesson(Long id, CreateLessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        lesson.setTitle(dto.getTitle());
        lesson.setSubject(dto.getSubject());
        lesson.setGrade(dto.getGrade());
        lesson.setContent(dto.getContent());

        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapToDTO(updatedLesson);
    }

    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
    }

    private LessonDTO mapToDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setSubject(lesson.getSubject());
        dto.setGrade(lesson.getGrade());
        dto.setContent(lesson.getContent());
        return dto;
    }
}

