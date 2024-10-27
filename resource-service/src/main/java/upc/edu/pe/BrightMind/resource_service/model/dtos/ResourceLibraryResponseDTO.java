package upc.edu.pe.BrightMind.resource_service.model.dtos;

import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;

@Getter
@Setter
public class ResourceLibraryResponseDTO {
    private Long id;
    private String title;
    private Subject subject;
    private Grade grade;
}

