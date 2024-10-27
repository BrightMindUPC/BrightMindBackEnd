package upc.edu.pe.BrightMind.user_service.model.entities.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Grade;
import upc.edu.pe.BrightMind.user_service.model.entities.User;

@Entity
@Getter
@Setter
public class StudentDetails {

    @Id
    private Long id; // Same as User ID

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public StudentDetails() {}

    public StudentDetails(Grade grade, User user) {
        this.grade = grade;
        this.user = user;
    }
}
