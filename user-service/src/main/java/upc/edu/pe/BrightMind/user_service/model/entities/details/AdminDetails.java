package upc.edu.pe.BrightMind.user_service.model.entities.details;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Department;
import upc.edu.pe.BrightMind.user_service.model.entities.User;

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

