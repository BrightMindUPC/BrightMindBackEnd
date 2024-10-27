package upc.edu.pe.BrightMind.lesson_service.model.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;

@Getter
@Setter
public class CreateLessonDTO {

    @NotBlank
    private String title;

    @NotNull
    private Subject subject;

    @NotNull
    private Grade grade;

    @NotBlank
    private String content;
}

