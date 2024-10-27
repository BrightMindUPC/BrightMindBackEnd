package upc.edu.pe.BrightMind.lesson_service.model.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateLearningPathDTO {

    @NotBlank
    private String name;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> lessonIds;
}

