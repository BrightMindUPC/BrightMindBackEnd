package upc.edu.pe.BrightMind.assessment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentRequestDTO;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentResponseDTO;
import upc.edu.pe.BrightMind.assessment_service.model.entities.Assessment;
import upc.edu.pe.BrightMind.assessment_service.repository.AssessmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    public AssessmentResponseDTO createAssessment(AssessmentRequestDTO requestDTO) {
        Assessment assessment = new Assessment(
                requestDTO.getUserID(),
                requestDTO.getScore(),
                requestDTO.getFeedback(),
                requestDTO.getCompletionDate()
        );
        assessment = assessmentRepository.save(assessment);
        return mapToDTO(assessment);
    }

    public AssessmentResponseDTO updateAssessment(Integer evalID, AssessmentRequestDTO requestDTO) {
        Assessment assessment = assessmentRepository.findById(evalID)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        assessment.setUserID(requestDTO.getUserID());
        assessment.setScore(requestDTO.getScore());
        assessment.setFeedback(requestDTO.getFeedback());
        assessment.setCompletionDate(requestDTO.getCompletionDate());
        assessment = assessmentRepository.save(assessment);
        return mapToDTO(assessment);
    }

    public AssessmentResponseDTO getAssessmentById(Integer evalID) {
        Assessment assessment = assessmentRepository.findById(evalID)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        return mapToDTO(assessment);
    }

    public List<AssessmentResponseDTO> getAllAssessments() {
        return assessmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAssessment(Integer evalID) {
        assessmentRepository.deleteById(evalID);
    }

    public List<AssessmentResponseDTO> getAssessmentsByUserId(Integer userID) {
        return assessmentRepository.findByUserID(userID).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AssessmentResponseDTO mapToDTO(Assessment assessment) {
        AssessmentResponseDTO dto = new AssessmentResponseDTO();
        dto.setEvalID(assessment.getEvalID());
        dto.setUserID(assessment.getUserID());
        dto.setScore(assessment.getScore());
        dto.setFeedback(assessment.getFeedback());
        dto.setCompletionDate(assessment.getCompletionDate());
        return dto;
    }
}

