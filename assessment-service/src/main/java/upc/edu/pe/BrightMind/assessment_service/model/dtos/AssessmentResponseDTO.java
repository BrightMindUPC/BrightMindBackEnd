package upc.edu.pe.BrightMind.assessment_service.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssessmentResponseDTO {
    private Integer evalID;
    private Integer userID;
    private float score;
    private String feedback;
    private LocalDate completionDate;
}
