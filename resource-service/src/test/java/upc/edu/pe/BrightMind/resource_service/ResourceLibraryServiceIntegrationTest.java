package upc.edu.pe.BrightMind.resource_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.ResourceLibrary;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;
import upc.edu.pe.BrightMind.resource_service.repository.ResourceLibraryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ResourceLibraryServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLibraryRepository resourceLibraryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        resourceLibraryRepository.deleteAll();  // Limpia la base de datos antes de cada test
    }

    // Test de integración para crear un recurso
    @Test
    void testCreateResource_Success() throws Exception {
        MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "test.pdf", MediaType.APPLICATION_PDF_VALUE, "Sample PDF content".getBytes());

        mockMvc.perform(multipart("/api/resources")
                        .file(pdfFile)
                        .param("title", "Sample Resource")
                        .param("subject", Subject.MATH.name())
                        .param("grade", Grade.FIRST_YEAR.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Resource"))
                .andExpect(jsonPath("$.subject").value("MATH"))
                .andExpect(jsonPath("$.grade").value("FIRST_YEAR"));
    }

    // Test de integración para obtener un recurso por ID
    @Test
    void testGetResourceById_Success() throws Exception {
        // Primero, crea un recurso directamente en el repositorio
        var resource = resourceLibraryRepository.save(new ResourceLibrary("Test Resource", Subject.SCIENCE, Grade.SECOND_YEAR, "PDF Content".getBytes()));

        mockMvc.perform(get("/api/resources/" + resource.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Resource"))
                .andExpect(jsonPath("$.subject").value("SCIENCE"))
                .andExpect(jsonPath("$.grade").value("SECOND_YEAR"));
    }

    // Test de integración para obtener un recurso por título
    @Test
    void testGetResourceByTitle_Success() throws Exception {
        var resource = resourceLibraryRepository.save(new ResourceLibrary("Unique Title", Subject.HISTORY, Grade.THIRD_YEAR, "PDF Content".getBytes()));

        mockMvc.perform(get("/api/resources/title/Unique Title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Unique Title"))
                .andExpect(jsonPath("$.subject").value("HISTORY"))
                .andExpect(jsonPath("$.grade").value("THIRD_YEAR"));
    }

    // Test de integración para descargar el PDF
    @Test
    void testDownloadPdf_Success() throws Exception {
        var resource = resourceLibraryRepository.save(new ResourceLibrary("Downloadable Resource", Subject.CHEMISTRY, Grade.FOURTH_YEAR, "PDF Content".getBytes()));

        mockMvc.perform(get("/api/resources/" + resource.getId() + "/download"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes("PDF Content".getBytes()));
    }

    // Test de integración para obtener recursos por grado
    @Test
    void testGetResourcesByGrade_Success() throws Exception {
        resourceLibraryRepository.save(new ResourceLibrary("Grade Resource 1", Subject.LITERATURE, Grade.FIFTH_YEAR, "PDF Content 1".getBytes()));
        resourceLibraryRepository.save(new ResourceLibrary("Grade Resource 2", Subject.PHYSICS, Grade.FIFTH_YEAR, "PDF Content 2".getBytes()));

        mockMvc.perform(get("/api/resources/grade/FIFTH_YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].grade").value("FIFTH_YEAR"))
                .andExpect(jsonPath("$[1].grade").value("FIFTH_YEAR"));
    }

    // Test de integración para obtener recursos por sujeto
    @Test
    void testGetResourcesBySubject_Success() throws Exception {
        resourceLibraryRepository.save(new ResourceLibrary("Math Resource", Subject.MATH, Grade.FIRST_YEAR, "PDF Content".getBytes()));

        mockMvc.perform(get("/api/resources/subject/MATH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].subject").value("MATH"));
    }

    // Test de integración para obtener recursos por grado y sujeto
    @Test
    void testGetResourcesByGradeAndSubject_Success() throws Exception {
        resourceLibraryRepository.save(new ResourceLibrary("Resource 1", Subject.SCIENCE, Grade.SECOND_YEAR, "Content 1".getBytes()));
        resourceLibraryRepository.save(new ResourceLibrary("Resource 2", Subject.SCIENCE, Grade.SECOND_YEAR, "Content 2".getBytes()));

        mockMvc.perform(get("/api/resources/grade/SECOND_YEAR/subject/SCIENCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].grade").value("SECOND_YEAR"))
                .andExpect(jsonPath("$[0].subject").value("SCIENCE"))
                .andExpect(jsonPath("$[1].grade").value("SECOND_YEAR"))
                .andExpect(jsonPath("$[1].subject").value("SCIENCE"));
    }
}

