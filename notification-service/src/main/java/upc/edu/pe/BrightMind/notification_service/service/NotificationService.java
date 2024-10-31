package upc.edu.pe.BrightMind.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationRequestDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.entities.Notification;
import upc.edu.pe.BrightMind.notification_service.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://user-service/api/users";

    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        // Intentar obtener la información del usuario desde user-service
        UserResponseDTO userResponseDTO = getUserInfo(notificationRequestDTO.getUserId());

        Notification notification = new Notification(
                notificationRequestDTO.getMessage(),
                notificationRequestDTO.getUserId()
        );
        notification = notificationRepository.save(notification);

        NotificationResponseDTO responseDTO = mapToDTO(notification);
        responseDTO.setUser(userResponseDTO); // Agregar info de usuario si está disponible
        return responseDTO;
    }

    private UserResponseDTO getUserInfo(Long userId) {
        try {
            // Llamada al microservicio de user-service usando Eureka
            return restTemplate.getForObject(USER_SERVICE_URL + "/" + userId, UserResponseDTO.class);
        } catch (RestClientException e) {
            // Manejar la excepción si el user-service no está disponible o el usuario no se encuentra
            System.out.println("User service is not available or user not found.");
            return null;
        }
    }

    // Método para marcar una notificación como leída
    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return mapToDTO(notification);
    }

    // Método para obtener todas las notificaciones de un usuario específico
    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private NotificationResponseDTO mapToDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setUserId(notification.getUserId());
        dto.setTimestamp(notification.getTimestamp());
        return dto;
    }
}
