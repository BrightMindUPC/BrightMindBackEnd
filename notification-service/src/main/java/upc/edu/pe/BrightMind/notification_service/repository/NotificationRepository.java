package upc.edu.pe.BrightMind.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.BrightMind.notification_service.model.entities.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}