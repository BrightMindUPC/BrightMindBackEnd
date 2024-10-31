package upc.edu.pe.BrightMind.notification_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    private boolean isRead;

    @Column(nullable = false)
    private Long userId; // Relación con el ID de usuario de otro microservicio

    private LocalDateTime timestamp;

    public Notification() {}

    public Notification(String message, Long userId) {
        this.message = message;
        this.userId = userId;
        this.isRead = false;
        this.timestamp = LocalDateTime.now();
    }
}

