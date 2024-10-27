package upc.edu.pe.BrightMind.lesson_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;
import upc.edu.pe.BrightMind.lesson_service.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@Valid @RequestBody CreateLessonDTO dto) {
        LessonDTO createdLesson = lessonService.createLesson(dto);
        return new ResponseEntity<>(createdLesson, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long id) {
        LessonDTO lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        List<LessonDTO> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<LessonDTO>> getLessonsBySubject(@PathVariable Subject subject) {
        List<LessonDTO> lessons = lessonService.getLessonsBySubject(subject);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<LessonDTO>> getLessonsByGrade(@PathVariable Grade grade) {
        List<LessonDTO> lessons = lessonService.getLessonsByGrade(grade);
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id,
                                                  @Valid @RequestBody CreateLessonDTO dto) {
        LessonDTO updatedLesson = lessonService.updateLesson(id, dto);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}

