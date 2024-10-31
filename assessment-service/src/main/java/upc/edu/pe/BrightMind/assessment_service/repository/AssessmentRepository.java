package upc.edu.pe.BrightMind.assessment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.BrightMind.assessment_service.model.entities.Assessment;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
    List<Assessment> findByUserID(Integer userID);
}

