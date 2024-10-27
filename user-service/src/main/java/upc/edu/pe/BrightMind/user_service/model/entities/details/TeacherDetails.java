package upc.edu.pe.BrightMind.user_service.model.entities.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Subject;
import upc.edu.pe.BrightMind.user_service.model.entities.User;

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

