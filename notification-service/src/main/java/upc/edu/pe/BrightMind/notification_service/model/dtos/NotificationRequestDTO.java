package upc.edu.pe.BrightMind.notification_service.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDTO {
    private String message;
    private Long userId;
}
