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
}
