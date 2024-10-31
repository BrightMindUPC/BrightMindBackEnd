package upc.edu.pe.BrightMind.notification_service.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDTO {
    private Long id;
    private String message;
    private boolean isRead;
    private Long userId;
    private LocalDateTime timestamp;
    private UserResponseDTO user; // Información del usuario si está disponible

    // Constructor vacío
    public NotificationResponseDTO() {}

    // Constructor completo
    public NotificationResponseDTO(Long id, String message, boolean isRead, Long userId, LocalDateTime timestamp, UserResponseDTO user) {
        this.id = id;
        this.message = message;
        this.isRead = isRead;
        this.userId = userId;
        this.timestamp = timestamp;
        this.user = user;
    }
}
