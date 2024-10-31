package upc.edu.pe.BrightMind.assessment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentRequestDTO;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentResponseDTO;
import upc.edu.pe.BrightMind.assessment_service.service.AssessmentService;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentResponseDTO> createAssessment(
            @RequestBody AssessmentRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assessmentService.createAssessment(requestDTO));
    }

    @PutMapping("/{evalID}")
    public ResponseEntity<AssessmentResponseDTO> updateAssessment(
            @PathVariable Integer evalID,
            @RequestBody AssessmentRequestDTO requestDTO) {
        return ResponseEntity.ok(assessmentService.updateAssessment(evalID, requestDTO));
    }

    @GetMapping("/{evalID}")
    public ResponseEntity<AssessmentResponseDTO> getAssessmentById(@PathVariable Integer evalID) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(evalID));
    }

    @GetMapping
    public ResponseEntity<List<AssessmentResponseDTO>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @DeleteMapping("/{evalID}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Integer evalID) {
        assessmentService.deleteAssessment(evalID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<List<AssessmentResponseDTO>> getAssessmentsByUserId(@PathVariable Integer userID) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByUserId(userID));
    }
}

