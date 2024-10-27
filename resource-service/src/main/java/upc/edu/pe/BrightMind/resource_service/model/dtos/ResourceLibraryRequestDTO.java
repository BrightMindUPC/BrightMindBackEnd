package upc.edu.pe.BrightMind.resource_service.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;

@Getter
@Setter
public class ResourceLibraryRequestDTO {

    private String title;
    private Subject subject;
    private Grade grade;
    private MultipartFile pdfFile;
}
