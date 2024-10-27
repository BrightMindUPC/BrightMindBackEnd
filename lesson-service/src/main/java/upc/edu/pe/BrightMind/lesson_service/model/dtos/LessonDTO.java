package upc.edu.pe.BrightMind.lesson_service.model.dtos;

import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;

@Getter
@Setter
public class LessonDTO {

    private Long id;
    private String title;
    private Subject subject;
    private Grade grade;
    private String content;
}

