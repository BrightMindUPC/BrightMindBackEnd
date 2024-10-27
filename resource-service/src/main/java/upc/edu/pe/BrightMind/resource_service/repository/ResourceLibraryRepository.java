package upc.edu.pe.BrightMind.resource_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.BrightMind.resource_service.model.entities.Grade;
import upc.edu.pe.BrightMind.resource_service.model.entities.ResourceLibrary;
import upc.edu.pe.BrightMind.resource_service.model.entities.Subject;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceLibraryRepository extends JpaRepository<ResourceLibrary, Long> {
    Optional<ResourceLibrary> findByTitle(String title);

    // Nuevos métodos
    List<ResourceLibrary> findByGrade(Grade grade);

    List<ResourceLibrary> findBySubject(Subject subject);

    List<ResourceLibrary> findByGradeAndSubject(Grade grade, Subject subject);
}