package upc.edu.pe.BrightMind.notification_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationRequestDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.entities.Notification;
import upc.edu.pe.BrightMind.notification_service.repository.NotificationRepository;
import upc.edu.pe.BrightMind.notification_service.util.CustomRestTemplateConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // Utiliza un perfil de prueba para evitar la configuración en producción
@Import(CustomRestTemplateConfig.class)
public class NotificationServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    @Qualifier("customRestTemplate")
    private RestTemplate restTemplate;


    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    public void setup() {
        notificationRepository.deleteAll();
    }

    @Test
    public void testGetNotificationsByUserId() {
        Notification notification1 = new Notification("Test message 1", 1L);
        Notification notification2 = new Notification("Test message 2", 1L);
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        String url = "http://localhost:" + port + "/api/notifications/user/1";

        ResponseEntity<NotificationResponseDTO[]> response = restTemplate.getForEntity(url, NotificationResponseDTO[].class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
        assertThat(response.getBody()[0].getMessage()).isEqualTo("Test message 1");
        assertThat(response.getBody()[1].getMessage()).isEqualTo("Test message 2");
    }

    @Test
    public void testMarkAsRead() {
        Notification notification = new Notification("Test message", 1L);
        notification.setRead(false);
        Notification savedNotification = notificationRepository.save(notification);

        String url = "http://localhost:" + port + "/api/notifications/" + savedNotification.getId() + "/mark-as-read";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<NotificationResponseDTO> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, NotificationResponseDTO.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isRead()).isTrue();

        Notification updatedNotification = notificationRepository.findById(savedNotification.getId()).orElse(null);
        assertThat(updatedNotification).isNotNull();
        assertThat(updatedNotification.isRead()).isTrue();
    }
}

