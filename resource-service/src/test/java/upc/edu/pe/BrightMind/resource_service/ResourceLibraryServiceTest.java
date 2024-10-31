package upc.edu.pe.BrightMind.resource_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryRequestDTO;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryResponseDTO;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.ResourceLibrary;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;
import upc.edu.pe.BrightMind.resource_service.repository.ResourceLibraryRepository;
import upc.edu.pe.BrightMind.resource_service.service.ResourceLibraryService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ResourceLibraryServiceTest {

    @Mock
    private ResourceLibraryRepository resourceLibraryRepository;

    @InjectMocks
    private ResourceLibraryService resourceLibraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para crear un recurso
    @Test
    void testCreateResource_Success() throws IOException {
        ResourceLibraryRequestDTO requestDTO = new ResourceLibraryRequestDTO();
        requestDTO.setTitle("Sample Resource");
        requestDTO.setSubject(Subject.MATH);
        requestDTO.setGrade(Grade.FIRST_YEAR);
        requestDTO.setPdfFile(new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", "Sample PDF content".getBytes()));

        ResourceLibrary savedResource = new ResourceLibrary("Sample Resource", Subject.MATH, Grade.FIRST_YEAR, "Sample PDF content".getBytes());
        savedResource.setId(1L);

        when(resourceLibraryRepository.save(any(ResourceLibrary.class))).thenReturn(savedResource);

        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.createResource(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Sample Resource", responseDTO.getTitle());
        assertEquals(Subject.MATH, responseDTO.getSubject());
        assertEquals(Grade.FIRST_YEAR, responseDTO.getGrade());
    }

    @Test
    void testCreateResource_MissingTitle() {
        ResourceLibraryRequestDTO requestDTO = new ResourceLibraryRequestDTO();
        requestDTO.setTitle("");
        requestDTO.setSubject(Subject.MATH);
        requestDTO.setGrade(Grade.FIRST_YEAR);
        requestDTO.setPdfFile(new MockMultipartFile("pdfFile", "test.pdf", "application/pdf", "Sample PDF content".getBytes()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> resourceLibraryService.createResource(requestDTO));
        assertEquals("El título es requerido", exception.getMessage());
    }

    // Test para obtener un recurso por ID
    @Test
    void testGetResourceById_Success() {
        ResourceLibrary resource = new ResourceLibrary("Sample Resource", Subject.MATH, Grade.FIRST_YEAR, "Sample PDF content".getBytes());
        resource.setId(1L);
        when(resourceLibraryRepository.findById(1L)).thenReturn(Optional.of(resource));

        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.getResourceById(1L);

        assertNotNull(responseDTO);
        assertEquals("Sample Resource", responseDTO.getTitle());
    }

    @Test
    void testGetResourceById_NotFound() {
        when(resourceLibraryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> resourceLibraryService.getResourceById(1L));
        assertEquals("Recurso no encontrado con el id: 1", exception.getMessage());
    }

    // Test para obtener un recurso por título
    @Test
    void testGetResourceByTitle_Success() {
        ResourceLibrary resource = new ResourceLibrary("Sample Resource", Subject.MATH, Grade.FIRST_YEAR, "Sample PDF content".getBytes());
        resource.setId(1L);
        when(resourceLibraryRepository.findByTitle("Sample Resource")).thenReturn(Optional.of(resource));

        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.getResourceByTitle("Sample Resource");

        assertNotNull(responseDTO);
        assertEquals("Sample Resource", responseDTO.getTitle());
    }

    @Test
    void testGetResourceByTitle_NotFound() {
        when(resourceLibraryRepository.findByTitle("Sample Resource")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> resourceLibraryService.getResourceByTitle("Sample Resource"));
        assertEquals("Recurso no encontrado con el título: Sample Resource", exception.getMessage());
    }

    // Test para descargar PDF por ID
    @Test
    void testDownloadPdf_Success() {
        ResourceLibrary resource = new ResourceLibrary("Sample Resource", Subject.MATH, Grade.FIRST_YEAR, "Sample PDF content".getBytes());
        resource.setId(1L);
        when(resourceLibraryRepository.findById(1L)).thenReturn(Optional.of(resource));

        byte[] pdfContent = resourceLibraryService.downloadPdf(1L);

        assertNotNull(pdfContent);
        assertArrayEquals("Sample PDF content".getBytes(), pdfContent);
    }

    @Test
    void testDownloadPdf_NotFound() {
        when(resourceLibraryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> resourceLibraryService.downloadPdf(1L));
        assertEquals("Recurso no encontrado con el id: 1", exception.getMessage());
    }

    // Test para obtener recursos por grado
    @Test
    void testGetResourcesByGrade() {
        List<ResourceLibrary> resources = Stream.of(
                new ResourceLibrary("Resource 1", Subject.MATH, Grade.FIRST_YEAR, "Content 1".getBytes()),
                new ResourceLibrary("Resource 2", Subject.SCIENCE, Grade.FIRST_YEAR, "Content 2".getBytes())
        ).collect(Collectors.toList());

        when(resourceLibraryRepository.findByGrade(Grade.FIRST_YEAR)).thenReturn(resources);

        List<ResourceLibraryResponseDTO> responseDTOs = resourceLibraryService.getResourcesByGrade(Grade.FIRST_YEAR);

        assertEquals(2, responseDTOs.size());
        assertEquals("Resource 1", responseDTOs.get(0).getTitle());
        assertEquals("Resource 2", responseDTOs.get(1).getTitle());
    }

    // Test para obtener recursos por sujeto
    @Test
    void testGetResourcesBySubject() {
        List<ResourceLibrary> resources = Stream.of(
                new ResourceLibrary("Resource 1", Subject.MATH, Grade.FIRST_YEAR, "Content 1".getBytes())
        ).collect(Collectors.toList());

        when(resourceLibraryRepository.findBySubject(Subject.MATH)).thenReturn(resources);

        List<ResourceLibraryResponseDTO> responseDTOs = resourceLibraryService.getResourcesBySubject(Subject.MATH);

        assertEquals(1, responseDTOs.size());
        assertEquals("Resource 1", responseDTOs.get(0).getTitle());
    }
}

