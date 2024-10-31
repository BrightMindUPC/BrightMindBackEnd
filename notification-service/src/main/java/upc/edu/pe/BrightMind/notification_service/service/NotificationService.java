package upc.edu.pe.BrightMind.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationRequestDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.entities.Notification;
import upc.edu.pe.BrightMind.notification_service.repository.NotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        Notification notification = new Notification(
                notificationRequestDTO.getMessage(),
                notificationRequestDTO.getUserId()
        );
        notification = notificationRepository.save(notification);
        return mapToDTO(notification);
    }

    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return mapToDTO(notification);
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

