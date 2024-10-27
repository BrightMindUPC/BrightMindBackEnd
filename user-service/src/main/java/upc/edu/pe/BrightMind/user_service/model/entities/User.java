package upc.edu.pe.BrightMind.user_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.user_service.model.entities.details.AdminDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.details.StudentDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.details.TeacherDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Campo único y no nulo
    private String username;

    @Column(unique = true, nullable = false) // Campo único y no nulo
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Relationships to detail entities
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentDetails studentDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeacherDetails teacherDetails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdminDetails adminDetails;

    // Constructors, getters, and setters
    public User() {}

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

