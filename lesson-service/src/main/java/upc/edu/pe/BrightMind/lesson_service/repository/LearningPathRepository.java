package upc.edu.pe.BrightMind.lesson_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.BrightMind.lesson_service.model.entities.LearningPath;

import java.util.List;

public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {

    List<LearningPath> findByUserId(Long userId);
}

