package upc.edu.pe.BrightMind.assessment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentRequestDTO;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentResponseDTO;
import upc.edu.pe.BrightMind.assessment_service.service.AssessmentService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AssessmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssessmentService assessmentService;

    @BeforeEach
    public void setup() {
        assessmentService.getAllAssessments().forEach(assessment -> assessmentService.deleteAssessment(assessment.getEvalID()));
    }

    @Test
    public void testCreateAssessment() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(85.5f);
        requestDTO.setFeedback("Good job!");
        requestDTO.setCompletionDate(LocalDate.now());

        mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userID").value(1))
                .andExpect(jsonPath("$.score").value(85.5))
                .andExpect(jsonPath("$.feedback").value("Good job!"));
    }

    @Test
    public void testUpdateAssessment() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(75.0f);
        requestDTO.setFeedback("Needs improvement");
        requestDTO.setCompletionDate(LocalDate.now());
        AssessmentResponseDTO createdAssessment = assessmentService.createAssessment(requestDTO);

        AssessmentRequestDTO updateDTO = new AssessmentRequestDTO();
        updateDTO.setUserID(1);
        updateDTO.setScore(90.0f);
        updateDTO.setFeedback("Improved significantly");
        updateDTO.setCompletionDate(LocalDate.now());

        mockMvc.perform(put("/api/assessments/" + createdAssessment.getEvalID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(90.0))
                .andExpect(jsonPath("$.feedback").value("Improved significantly"));
    }

    @Test
    public void testGetAssessmentById() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(2);
        requestDTO.setScore(65.0f);
        requestDTO.setFeedback("Fair performance");
        requestDTO.setCompletionDate(LocalDate.now());
        AssessmentResponseDTO createdAssessment = assessmentService.createAssessment(requestDTO);

        mockMvc.perform(get("/api/assessments/" + createdAssessment.getEvalID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userID").value(2))
                .andExpect(jsonPath("$.score").value(65.0))
                .andExpect(jsonPath("$.feedback").value("Fair performance"));
    }

    @Test
    public void testGetAllAssessments() throws Exception {
        AssessmentRequestDTO requestDTO1 = new AssessmentRequestDTO();
        requestDTO1.setUserID(1);
        requestDTO1.setScore(80.0f);
        requestDTO1.setFeedback("Well done");
        requestDTO1.setCompletionDate(LocalDate.now());
        assessmentService.createAssessment(requestDTO1);

        AssessmentRequestDTO requestDTO2 = new AssessmentRequestDTO();
        requestDTO2.setUserID(2);
        requestDTO2.setScore(90.0f);
        requestDTO2.setFeedback("Excellent");
        requestDTO2.setCompletionDate(LocalDate.now());
        assessmentService.createAssessment(requestDTO2);

        mockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testDeleteAssessment() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(3);
        requestDTO.setScore(70.0f);
        requestDTO.setFeedback("Satisfactory");
        requestDTO.setCompletionDate(LocalDate.now());
        AssessmentResponseDTO createdAssessment = assessmentService.createAssessment(requestDTO);

        mockMvc.perform(delete("/api/assessments/" + createdAssessment.getEvalID()))
                .andExpect(status().isNoContent());

        assertThat(assessmentService.getAllAssessments()).doesNotContain(createdAssessment);
    }

    @Test
    public void testGetAssessmentsByUserId() throws Exception {
        AssessmentRequestDTO requestDTO1 = new AssessmentRequestDTO();
        requestDTO1.setUserID(4);
        requestDTO1.setScore(88.0f);
        requestDTO1.setFeedback("Very good");
        requestDTO1.setCompletionDate(LocalDate.now());
        assessmentService.createAssessment(requestDTO1);

        AssessmentRequestDTO requestDTO2 = new AssessmentRequestDTO();
        requestDTO2.setUserID(4);
        requestDTO2.setScore(92.0f);
        requestDTO2.setFeedback("Excellent");
        requestDTO2.setCompletionDate(LocalDate.now());
        assessmentService.createAssessment(requestDTO2);

        mockMvc.perform(get("/api/assessments/user/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}

