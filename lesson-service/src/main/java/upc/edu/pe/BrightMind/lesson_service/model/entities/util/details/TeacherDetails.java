package upc.edu.pe.BrightMind.lesson_service.model.entities.util.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.User;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Subject;


@Entity
@Getter
@Setter
public class TeacherDetails {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public TeacherDetails() {}

    public TeacherDetails(Subject subject, User user) {
        this.subject = subject;
        this.user = user;
    }
}

