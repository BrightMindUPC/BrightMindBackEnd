package upc.edu.pe.BrightMind.lesson_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.BrightMind.lesson_service.model.entities.Lesson;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findBySubject(Subject subject);

    List<Lesson> findByGrade(Grade grade);
}

