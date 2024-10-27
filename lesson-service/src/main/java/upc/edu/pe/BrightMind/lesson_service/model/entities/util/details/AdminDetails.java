package upc.edu.pe.BrightMind.lesson_service.model.entities.util.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.User;
import upc.edu.pe.BrightMind.lesson_service.model.entities.util.enums.Department;


@Entity
@Getter
@Setter
public class AdminDetails {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Department department;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    public AdminDetails() {}

    public AdminDetails(Department department, User user) {
        this.department = department;
        this.user = user;
    }
}

