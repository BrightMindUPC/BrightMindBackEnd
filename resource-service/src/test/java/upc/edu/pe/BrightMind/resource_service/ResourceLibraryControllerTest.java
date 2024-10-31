package upc.edu.pe.BrightMind.resource_service;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import upc.edu.pe.BrightMind.resource_service.controller.ResourceLibraryController;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryRequestDTO;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryResponseDTO;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;
import upc.edu.pe.BrightMind.resource_service.service.ResourceLibraryService;

import java.util.List;

@WebMvcTest(ResourceLibraryController.class)
public class ResourceLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceLibraryService resourceLibraryService;

    @Autowired
    private ObjectMapper objectMapper;

    // Constructor
    public ResourceLibraryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateResource() throws Exception {
        MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", "Sample PDF content".getBytes());

        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.createResource(any(ResourceLibraryRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(multipart("/api/resources")
                        .file(pdfFile)
                        .param("title", "Sample Resource")
                        .param("subject", Subject.MATH.name())
                        .param("grade", Grade.FIRST_YEAR.name()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Resource"))
                .andExpect(jsonPath("$.subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$.grade").value(Grade.FIRST_YEAR.name()));
    }

    @Test
    public void testGetResourceById() throws Exception {
        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.getResourceById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Resource"))
                .andExpect(jsonPath("$.subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$.grade").value(Grade.FIRST_YEAR.name()));
    }

    @Test
    public void testGetResourceByTitle() throws Exception {
        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.getResourceByTitle("Sample Resource")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/resources/title/Sample Resource"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Resource"))
                .andExpect(jsonPath("$.subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$.grade").value(Grade.FIRST_YEAR.name()));
    }

    @Test
    public void testDownloadPdf() throws Exception {
        byte[] pdfContent = "Sample PDF content".getBytes();
        when(resourceLibraryService.downloadPdf(1L)).thenReturn(pdfContent);

        mockMvc.perform(get("/api/resources/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"resource_1.pdf\""))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfContent));
    }

    @Test
    public void testGetResourcesByGrade() throws Exception {
        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.getResourcesByGrade(Grade.FIRST_YEAR)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/resources/grade/FIRST_YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Resource"))
                .andExpect(jsonPath("$[0].subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$[0].grade").value(Grade.FIRST_YEAR.name()));
    }

    @Test
    public void testGetResourcesBySubject() throws Exception {
        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.getResourcesBySubject(Subject.MATH)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/resources/subject/MATH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Resource"))
                .andExpect(jsonPath("$[0].subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$[0].grade").value(Grade.FIRST_YEAR.name()));
    }

    @Test
    public void testGetResourcesByGradeAndSubject() throws Exception {
        ResourceLibraryResponseDTO responseDTO = new ResourceLibraryResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Sample Resource");
        responseDTO.setSubject(Subject.MATH);
        responseDTO.setGrade(Grade.FIRST_YEAR);

        when(resourceLibraryService.getResourcesByGradeAndSubject(Grade.FIRST_YEAR, Subject.MATH)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/resources/grade/FIRST_YEAR/subject/MATH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample Resource"))
                .andExpect(jsonPath("$[0].subject").value(Subject.MATH.name()))
                .andExpect(jsonPath("$[0].grade").value(Grade.FIRST_YEAR.name()));
    }
}