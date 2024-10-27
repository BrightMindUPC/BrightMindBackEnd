package upc.edu.pe.BrightMind.chatbot_service.model.entities.util.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.User;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.enums.Grade;

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
