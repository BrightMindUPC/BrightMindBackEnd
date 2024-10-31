package upc.edu.pe.BrightMind.lesson_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import upc.edu.pe.BrightMind.lesson_service.controller.LessonController;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.CreateLessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.dtos.LessonDTO;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;
import upc.edu.pe.BrightMind.lesson_service.service.LessonService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateLesson() throws Exception {
        CreateLessonDTO requestDTO = new CreateLessonDTO();
        requestDTO.setTitle("Lesson 1");
        requestDTO.setSubject(Subject.MATH);
        requestDTO.setGrade(Grade.FIRST_YEAR);
        requestDTO.setContent("Math content");

        LessonDTO responseDTO = new LessonDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Lesson 1");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);
        responseDTO.setContent("Math content");

        when(lessonService.createLesson(any(CreateLessonDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Lesson 1"))
                .andExpect(jsonPath("$.subject").value("MATH"))
                .andExpect(jsonPath("$.grade").value("FIRST_YEAR"))
                .andExpect(jsonPath("$.content").value("Math content"));
    }

    @Test
    public void testGetLessonById() throws Exception {
        LessonDTO responseDTO = new LessonDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Lesson 1");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);
        responseDTO.setContent("Math content");

        when(lessonService.getLessonById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Lesson 1"))
                .andExpect(jsonPath("$.subject").value("MATH"))
                .andExpect(jsonPath("$.grade").value("FIRST_YEAR"))
                .andExpect(jsonPath("$.content").value("Math content"));
    }

    @Test
    public void testGetAllLessons() throws Exception {
        LessonDTO responseDTO1 = new LessonDTO();
        responseDTO1.setId(1L);
        responseDTO1.setTitle("Lesson 1");

        LessonDTO responseDTO2 = new LessonDTO();
        responseDTO2.setId(2L);
        responseDTO2.setTitle("Lesson 2");

        List<LessonDTO> responseDTOs = Arrays.asList(responseDTO1, responseDTO2);

        when(lessonService.getAllLessons()).thenReturn(responseDTOs);

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Lesson 1"))
                .andExpect(jsonPath("$[1].title").value("Lesson 2"));
    }

    @Test
    public void testGetLessonsBySubject() throws Exception {
        LessonDTO responseDTO = new LessonDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Lesson 1");
        responseDTO.setSubject(Subject.MATH);

        when(lessonService.getLessonsBySubject(Subject.MATH)).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/lessons/subject/MATH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Lesson 1"))
                .andExpect(jsonPath("$[0].subject").value("MATH"));
    }

    @Test
    public void testGetLessonsByGrade() throws Exception {
        LessonDTO responseDTO = new LessonDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Lesson 1");
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(lessonService.getLessonsByGrade(Grade.FIRST_YEAR)).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/lessons/grade/FIRST_YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Lesson 1"))
                .andExpect(jsonPath("$[0].grade").value("FIRST_YEAR"));
    }

    @Test
    public void testUpdateLesson() throws Exception {
        CreateLessonDTO requestDTO = new CreateLessonDTO();
        requestDTO.setTitle("Updated Lesson");
        requestDTO.setSubject(Subject.MATH);
        requestDTO.setGrade(Grade.SECOND_YEAR);
        requestDTO.setContent("Updated content");

        LessonDTO responseDTO = new LessonDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Updated Lesson");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.SECOND_YEAR);
        responseDTO.setContent("Updated content");

        when(lessonService.updateLesson(eq(1L), any(CreateLessonDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Lesson"))
                .andExpect(jsonPath("$.subject").value("MATH"))
                .andExpect(jsonPath("$.grade").value("SECOND_YEAR"))
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    public void testDeleteLesson() throws Exception {
        mockMvc.perform(delete("/api/lessons/1"))
                .andExpect(status().isNoContent());
    }
}

