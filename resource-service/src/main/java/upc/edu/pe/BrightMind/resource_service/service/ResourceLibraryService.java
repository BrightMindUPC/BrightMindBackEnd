package upc.edu.pe.BrightMind.resource_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryRequestDTO;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryResponseDTO;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.ResourceLibrary;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;
import upc.edu.pe.BrightMind.resource_service.repository.ResourceLibraryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceLibraryService {

    @Autowired
    private ResourceLibraryRepository resourceLibraryRepository;

    @Transactional
    public ResourceLibraryResponseDTO createResource(ResourceLibraryRequestDTO dto) throws IOException {
        // Validate fields manually if necessary
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El título es requerido");
        }
        if (dto.getSubject() == null) {
            throw new IllegalArgumentException("El sujeto es requerido");
        }
        if (dto.getGrade() == null) {
            throw new IllegalArgumentException("El grado es requerido");
        }
        if (dto.getPdfFile() == null || dto.getPdfFile().isEmpty()) {
            throw new IllegalArgumentException("El archivo PDF es requerido");
        }

        // Convert MultipartFile to byte[]
        byte[] pdfContent = dto.getPdfFile().getBytes();

        // Create the entity
        ResourceLibrary resource = new ResourceLibrary(
                dto.getTitle(),
                dto.getSubject(),
                dto.getGrade(),
                pdfContent
        );

        ResourceLibrary savedResource = resourceLibraryRepository.save(resource);
        return convertToResponseDTO(savedResource);
    }

    public ResourceLibraryResponseDTO getResourceById(Long id) {
        Optional<ResourceLibrary> optionalResource = resourceLibraryRepository.findById(id);
        if (!optionalResource.isPresent()) {
            throw new IllegalArgumentException("Recurso no encontrado con el id: " + id);
        }
        return convertToResponseDTO(optionalResource.get());
    }

    public ResourceLibraryResponseDTO getResourceByTitle(String title) {
        Optional<ResourceLibrary> optionalResource = resourceLibraryRepository.findByTitle(title);
        if (!optionalResource.isPresent()) {
            throw new IllegalArgumentException("Recurso no encontrado con el título: " + title);
        }
        return convertToResponseDTO(optionalResource.get());
    }

    public byte[] downloadPdf(Long id) {
        Optional<ResourceLibrary> optionalResource = resourceLibraryRepository.findById(id);
        if (!optionalResource.isPresent()) {
            throw new IllegalArgumentException("Recurso no encontrado con el id: " + id);
        }
        ResourceLibrary resource = optionalResource.get();
        return resource.getPdfContent();
    }

    private ResourceLibraryResponseDTO convertToResponseDTO(ResourceLibrary resource) {
        ResourceLibraryResponseDTO dto = new ResourceLibraryResponseDTO();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setSubject(resource.getSubject());
        dto.setGrade(resource.getGrade());
        return dto;
    }

    public List<ResourceLibraryResponseDTO> getResourcesByGrade(Grade grade) {
        List<ResourceLibrary> resources = resourceLibraryRepository.findByGrade(grade);
        return resources.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceLibraryResponseDTO> getResourcesBySubject(Subject subject) {
        List<ResourceLibrary> resources = resourceLibraryRepository.findBySubject(subject);
        return resources.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceLibraryResponseDTO> getResourcesByGradeAndSubject(Grade grade, Subject subject) {
        List<ResourceLibrary> resources = resourceLibraryRepository.findByGradeAndSubject(grade, subject);
        return resources.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}