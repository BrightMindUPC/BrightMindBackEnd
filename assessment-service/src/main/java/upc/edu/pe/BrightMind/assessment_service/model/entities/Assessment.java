package upc.edu.pe.BrightMind.assessment_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "assessments")
@Getter
@Setter
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer evalID; // Identificador único de la autoevaluación

    @Column(nullable = false)
    private Integer userID; // Identificador del estudiante que realizó la autoevaluación

    @Column(nullable = false)
    private float score; // Puntaje obtenido en la autoevaluación

    private String feedback; // Retroalimentación basada en el puntaje

    @Column(nullable = false)
    private LocalDate completionDate; // Fecha en la que se completó la autoevaluación

    public Assessment() {}

    public Assessment(Integer userID, float score, String feedback, LocalDate completionDate) {
        this.userID = userID;
        this.score = score;
        this.feedback = feedback;
        this.completionDate = completionDate;
    }
}

