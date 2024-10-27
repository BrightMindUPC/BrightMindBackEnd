package upc.edu.pe.BrightMind.lesson_service.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LearningPathDTO {

    private Long id;
    private String name;
    private Long userId;
    private List<LessonDTO> lessons;
}

