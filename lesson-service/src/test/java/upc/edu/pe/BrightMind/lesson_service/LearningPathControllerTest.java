package upc.edu.pe.BrightMind.lesson_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import upc.edu.pe.BrightMind.lesson_service.controller.LearningPathController;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LearningPathDTO;
import upc.edu.pe.BrightMind.lesson_service.service.LearningPathService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LearningPathController.class)
public class LearningPathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LearningPathService learningPathService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateLearningPath() throws Exception {
        CreateLearningPathDTO requestDTO = new CreateLearningPathDTO();
        requestDTO.setName("Java Basics");
        requestDTO.setUserId(1L);
        requestDTO.setLessonIds(Arrays.asList(1L, 2L));

        LearningPathDTO responseDTO = new LearningPathDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Java Basics");
        responseDTO.setUserId(1L);

        when(learningPathService.createLearningPath(any(CreateLearningPathDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/learning-paths")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Basics"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    public void testGetLearningPathById() throws Exception {
        LearningPathDTO responseDTO = new LearningPathDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Java Basics");
        responseDTO.setUserId(1L);

        when(learningPathService.getLearningPathById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/learning-paths/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Basics"));
    }

    @Test
    public void testGetAllLearningPaths() throws Exception {
        LearningPathDTO responseDTO1 = new LearningPathDTO();
        responseDTO1.setId(1L);
        responseDTO1.setName("Java Basics");

        LearningPathDTO responseDTO2 = new LearningPathDTO();
        responseDTO2.setId(2L);
        responseDTO2.setName("Python Basics");

        List<LearningPathDTO> responseDTOs = Arrays.asList(responseDTO1, responseDTO2);

        when(learningPathService.getAllLearningPaths()).thenReturn(responseDTOs);

        mockMvc.perform(get("/api/learning-paths"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Java Basics"))
                .andExpect(jsonPath("$[1].name").value("Python Basics"));
    }

    @Test
    public void testGetLearningPathsByUserId() throws Exception {
        LearningPathDTO responseDTO = new LearningPathDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Java Basics");
        responseDTO.setUserId(1L);

        when(learningPathService.getLearningPathsByUserId(1L)).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/learning-paths/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Java Basics"));
    }

    @Test
    public void testUpdateLearningPath() throws Exception {
        CreateLearningPathDTO requestDTO = new CreateLearningPathDTO();
        requestDTO.setName("Updated Java Basics");
        requestDTO.setUserId(1L);
        requestDTO.setLessonIds(Arrays.asList(1L, 2L));

        LearningPathDTO responseDTO = new LearningPathDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Updated Java Basics");
        responseDTO.setUserId(1L);

        when(learningPathService.updateLearningPath(eq(1L), any(CreateLearningPathDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/learning-paths/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Java Basics"));
    }

    @Test
    public void testDeleteLearningPath() throws Exception {
        mockMvc.perform(delete("/api/learning-paths/1"))
                .andExpect(status().isNoContent());
    }
}

