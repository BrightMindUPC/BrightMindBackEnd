package upc.edu.pe.BrightMind.lesson_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Grade;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;

@Entity
@Table(name = "lessons")
@Getter
@Setter
public class Lesson {

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
    private String content;

    // Constructors
    public Lesson() {}

    public Lesson(String title, Subject subject, Grade grade, String content) {
        this.title = title;
        this.subject = subject;
        this.grade = grade;
        this.content = content;
    }
}

