package upc.edu.pe.BrightMind.assessment_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentRequestDTO;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentResponseDTO;
import upc.edu.pe.BrightMind.assessment_service.model.entities.Assessment;
import upc.edu.pe.BrightMind.assessment_service.repository.AssessmentRepository;
import upc.edu.pe.BrightMind.assessment_service.service.AssessmentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;

    @Mock
    private AssessmentRepository assessmentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAssessment() {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(85.0f);
        requestDTO.setFeedback("Well done!");
        requestDTO.setCompletionDate(LocalDate.now());

        Assessment assessment = new Assessment(1, 85.0f, "Well done!", LocalDate.now());
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        AssessmentResponseDTO response = assessmentService.createAssessment(requestDTO);

        assertThat(response.getUserID()).isEqualTo(1);
        assertThat(response.getScore()).isEqualTo(85.0f);
        assertThat(response.getFeedback()).isEqualTo("Well done!");
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    public void testUpdateAssessment() {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(90.0f);
        requestDTO.setFeedback("Improved");
        requestDTO.setCompletionDate(LocalDate.now());

        Assessment assessment = new Assessment(1, 85.0f, "Good", LocalDate.now());
        when(assessmentRepository.findById(anyInt())).thenReturn(Optional.of(assessment));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        AssessmentResponseDTO response = assessmentService.updateAssessment(1, requestDTO);

        assertThat(response.getScore()).isEqualTo(90.0f);
        assertThat(response.getFeedback()).isEqualTo("Improved");
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    public void testGetAssessmentById() {
        Assessment assessment = new Assessment(1, 80.0f, "Fair", LocalDate.now());
        when(assessmentRepository.findById(anyInt())).thenReturn(Optional.of(assessment));

        AssessmentResponseDTO response = assessmentService.getAssessmentById(1);

        assertThat(response.getScore()).isEqualTo(80.0f);
        assertThat(response.getFeedback()).isEqualTo("Fair");
        verify(assessmentRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testGetAllAssessments() {
        Assessment assessment1 = new Assessment(1, 80.0f, "Fair", LocalDate.now());
        Assessment assessment2 = new Assessment(2, 90.0f, "Excellent", LocalDate.now());

        when(assessmentRepository.findAll()).thenReturn(Arrays.asList(assessment1, assessment2));

        List<AssessmentResponseDTO> responses = assessmentService.getAllAssessments();

        assertThat(responses).hasSize(2);
        verify(assessmentRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteAssessment() {
        doNothing().when(assessmentRepository).deleteById(anyInt());

        assessmentService.deleteAssessment(1);

        verify(assessmentRepository, times(1)).deleteById(anyInt());
    }
}

