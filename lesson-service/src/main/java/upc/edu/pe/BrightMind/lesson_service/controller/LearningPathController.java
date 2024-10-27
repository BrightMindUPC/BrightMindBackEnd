package upc.edu.pe.BrightMind.lesson_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.service.LearningPathService;

import java.util.List;

@RestController
@RequestMapping("/api/learning-paths")
public class LearningPathController {

    private final LearningPathService learningPathService;

    public LearningPathController(LearningPathService learningPathService) {
        this.learningPathService = learningPathService;
    }

    @PostMapping
    public ResponseEntity<LearningPathDTO> createLearningPath(@Valid @RequestBody CreateLearningPathDTO dto) {
        LearningPathDTO createdLearningPath = learningPathService.createLearningPath(dto);
        return new ResponseEntity<>(createdLearningPath, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPathDTO> getLearningPathById(@PathVariable Long id) {
        LearningPathDTO learningPath = learningPathService.getLearningPathById(id);
        return ResponseEntity.ok(learningPath);
    }

    @GetMapping
    public ResponseEntity<List<LearningPathDTO>> getAllLearningPaths() {
        List<LearningPathDTO> learningPaths = learningPathService.getAllLearningPaths();
        return ResponseEntity.ok(learningPaths);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LearningPathDTO>> getLearningPathsByUserId(@PathVariable Long userId) {
        List<LearningPathDTO> learningPaths = learningPathService.getLearningPathsByUserId(userId);
        return ResponseEntity.ok(learningPaths);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPathDTO> updateLearningPath(@PathVariable Long id,
                                                              @Valid @RequestBody CreateLearningPathDTO dto) {
        LearningPathDTO updatedLearningPath = learningPathService.updateLearningPath(id, dto);
        return ResponseEntity.ok(updatedLearningPath);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPath(@PathVariable Long id) {
        learningPathService.deleteLearningPath(id);
        return ResponseEntity.noContent().build();
    }
}
