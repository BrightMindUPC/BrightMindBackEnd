package upc.edu.pe.BrightMind.resource_service.repository;


import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryMinimalDTO;
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

    @Query("SELECT new upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryMinimalDTO(r.id, r.title, r.subject, r.grade) FROM ResourceLibrary r WHERE r.grade = :grade")
    List<ResourceLibraryMinimalDTO> findByGradeExcludePdf(@Param("grade") Grade grade);

    @Query("SELECT new upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryMinimalDTO(r.id, r.title, r.subject, r.grade) FROM ResourceLibrary r WHERE r.subject = :subject")
    List<ResourceLibraryMinimalDTO> findBySubjectExcludePdf(@Param("subject") Subject subject);

    @Query("SELECT new upc.edu.pe.BrightMind.resource_service.model.dtos.ResourceLibraryMinimalDTO(r.id, r.title, r.subject, r.grade) FROM ResourceLibrary r WHERE r.grade = :grade AND r.subject = :subject")
    List<ResourceLibraryMinimalDTO> findByGradeAndSubjectExcludePdf(@Param("grade") Grade grade, @Param("subject") Subject subject);
}
