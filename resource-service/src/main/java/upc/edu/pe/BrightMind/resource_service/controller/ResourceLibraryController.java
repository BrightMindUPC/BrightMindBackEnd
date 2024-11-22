package upc.edu.pe.BrightMind.resource_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryMinimalDTO;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryRequestDTO;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryResponseDTO;

import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;
import upc.edu.pe.BrightMind.resource_service.service.ResourceLibraryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceLibraryController {

    @Autowired
    private ResourceLibraryService resourceLibraryService;

    // Endpoint para visualizar el PDF directamente en el navegador
    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> viewPdf(@PathVariable Long id) {
        byte[] pdfBytes = resourceLibraryService.downloadPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Obtener todos los recursos
    @GetMapping
    public ResponseEntity<List<ResourceLibraryResponseDTO>> getAllResources() {
        List<ResourceLibraryResponseDTO> resources = resourceLibraryService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    // Endpoint para crear un nuevo recurso
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceLibraryResponseDTO> createResource(
            @RequestParam("title") String title,
            @RequestParam("subject") Subject subject,
            @RequestParam("grade") Grade grade,
            @RequestParam("pdfFile") MultipartFile pdfFile) throws IOException {

        ResourceLibraryRequestDTO dto = new ResourceLibraryRequestDTO();
        dto.setTitle(title);
        dto.setSubject(subject);
        dto.setGrade(grade);
        dto.setPdfFile(pdfFile);

        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.createResource(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // Obtener recurso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResourceLibraryResponseDTO> getResourceById(@PathVariable Long id) {
        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.getResourceById(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Obtener recurso por título
    @GetMapping("/title/{title}")
    public ResponseEntity<ResourceLibraryResponseDTO> getResourceByTitle(@PathVariable String title) {
        ResourceLibraryResponseDTO responseDTO = resourceLibraryService.getResourceByTitle(title);
        return ResponseEntity.ok(responseDTO);
    }

    // Descargar PDF por ID
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdfBytes = resourceLibraryService.downloadPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("resource_" + id + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<ResourceLibraryMinimalDTO>> getResourcesByGrade(@PathVariable Grade grade) {
        List<ResourceLibraryMinimalDTO> resources = resourceLibraryService.getResourcesByGrade(grade);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<List<ResourceLibraryMinimalDTO>> getResourcesBySubject(@PathVariable Subject subject) {
        List<ResourceLibraryMinimalDTO> resources = resourceLibraryService.getResourcesBySubject(subject);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/grade/{grade}/subject/{subject}")
    public ResponseEntity<List<ResourceLibraryMinimalDTO>> getResourcesByGradeAndSubject(
            @PathVariable Grade grade,
            @PathVariable Subject subject) {
        List<ResourceLibraryMinimalDTO> resources = resourceLibraryService.getResourcesByGradeAndSubject(grade, subject);
        return ResponseEntity.ok(resources);
    }
}
