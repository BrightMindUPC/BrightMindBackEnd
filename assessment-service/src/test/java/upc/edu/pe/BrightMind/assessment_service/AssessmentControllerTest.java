package upc.edu.pe.BrightMind.assessment_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import upc.edu.pe.BrightMind.assessment_service.controller.AssessmentController;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentRequestDTO;
import upc.edu.pe.BrightMind.assessment_service.model.dtos.AssessmentResponseDTO;
import upc.edu.pe.BrightMind.assessment_service.service.AssessmentService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AssessmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Registro del módulo para LocalDate
    }

    @Test
    public void testCreateAssessment() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(85.0f);
        requestDTO.setFeedback("Great job!");
        requestDTO.setCompletionDate(LocalDate.now());

        AssessmentResponseDTO responseDTO = new AssessmentResponseDTO();
        responseDTO.setEvalID(1);
        responseDTO.setUserID(1);
        responseDTO.setScore(85.0f);
        responseDTO.setFeedback("Great job!");
        responseDTO.setCompletionDate(LocalDate.now());

        when(assessmentService.createAssessment(any(AssessmentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userID").value(1))
                .andExpect(jsonPath("$.score").value(85.0))
                .andExpect(jsonPath("$.feedback").value("Great job!"));
    }

    @Test
    public void testUpdateAssessment() throws Exception {
        AssessmentRequestDTO requestDTO = new AssessmentRequestDTO();
        requestDTO.setUserID(1);
        requestDTO.setScore(90.0f);
        requestDTO.setFeedback("Well improved!");
        requestDTO.setCompletionDate(LocalDate.now());

        AssessmentResponseDTO responseDTO = new AssessmentResponseDTO();
        responseDTO.setEvalID(1);
        responseDTO.setUserID(1);
        responseDTO.setScore(90.0f);
        responseDTO.setFeedback("Well improved!");
        responseDTO.setCompletionDate(LocalDate.now());

        when(assessmentService.updateAssessment(anyInt(), any(AssessmentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/assessments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(90.0))
                .andExpect(jsonPath("$.feedback").value("Well improved!"));
    }

    @Test
    public void testGetAssessmentById() throws Exception {
        AssessmentResponseDTO responseDTO = new AssessmentResponseDTO();
        responseDTO.setEvalID(1);
        responseDTO.setUserID(2);
        responseDTO.setScore(70.0f);
        responseDTO.setFeedback("Satisfactory");
        responseDTO.setCompletionDate(LocalDate.now());

        when(assessmentService.getAssessmentById(anyInt())).thenReturn(responseDTO);

        mockMvc.perform(get("/api/assessments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userID").value(2))
                .andExpect(jsonPath("$.score").value(70.0))
                .andExpect(jsonPath("$.feedback").value("Satisfactory"));
    }

    @Test
    public void testGetAllAssessments() throws Exception {
        AssessmentResponseDTO response1 = new AssessmentResponseDTO();
        response1.setEvalID(1);
        response1.setUserID(1);
        response1.setScore(75.0f);

        AssessmentResponseDTO response2 = new AssessmentResponseDTO();
        response2.setEvalID(2);
        response2.setUserID(2);
        response2.setScore(85.0f);

        when(assessmentService.getAllAssessments()).thenReturn(Arrays.asList(response1, response2));

        mockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testDeleteAssessment() throws Exception {
        doNothing().when(assessmentService).deleteAssessment(anyInt());

        mockMvc.perform(delete("/api/assessments/1"))
                .andExpect(status().isNoContent());

        verify(assessmentService, times(1)).deleteAssessment(anyInt());
    }
}
