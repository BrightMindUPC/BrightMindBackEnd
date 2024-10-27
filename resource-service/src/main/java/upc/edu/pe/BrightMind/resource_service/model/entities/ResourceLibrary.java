package upc.edu.pe.BrightMind.resource_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resource_library")
@Getter
@Setter
public class ResourceLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Lob
    @Column(nullable = false)
    private byte[] pdfContent;

    public ResourceLibrary() {}

    public ResourceLibrary(String title, Subject subject, Grade grade, byte[] pdfContent) {
        this.title = title;
        this.subject = subject;
        this.grade = grade;
        this.pdfContent = pdfContent;
    }
}
