package upc.edu.pe.BrightMind.notification_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationRequestDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.entities.Notification;
import upc.edu.pe.BrightMind.notification_service.repository.NotificationRepository;
import upc.edu.pe.BrightMind.notification_service.service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private static final String USER_SERVICE_URL = "http://user-service/api/users";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNotification() {
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setMessage("Test message");
        requestDTO.setUserId(1L);

        Notification savedNotification = new Notification("Test message", 1L);
        savedNotification.setId(1L);
        savedNotification.setTimestamp(LocalDateTime.now());

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testUser");
        userResponseDTO.setEmail("test@example.com");

        when(restTemplate.getForObject(USER_SERVICE_URL + "/1", UserResponseDTO.class)).thenReturn(userResponseDTO);
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        NotificationResponseDTO responseDTO = notificationService.createNotification(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Test message", responseDTO.getMessage());
        assertEquals(1L, responseDTO.getUserId());
        assertEquals(false, responseDTO.isRead());
        assertEquals("testUser", responseDTO.getUser().getUsername());
        assertEquals("test@example.com", responseDTO.getUser().getEmail());

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(restTemplate, times(1)).getForObject(USER_SERVICE_URL + "/1", UserResponseDTO.class);
    }

    @Test
    public void testMarkAsRead() {
        Notification notification = new Notification("Test message", 1L);
        notification.setId(1L);
        notification.setTimestamp(LocalDateTime.now());

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponseDTO responseDTO = notificationService.markAsRead(1L);

        assertNotNull(responseDTO);
        assertEquals(true, responseDTO.isRead());
        assertEquals("Test message", responseDTO.getMessage());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    public void testGetNotificationsByUserId() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testUser");
        userResponseDTO.setEmail("test@example.com");

        Notification notification1 = new Notification("Test message 1", 1L);
        notification1.setId(1L);
        notification1.setTimestamp(LocalDateTime.now());

        Notification notification2 = new Notification("Test message 2", 1L);
        notification2.setId(2L);
        notification2.setTimestamp(LocalDateTime.now());
        notification2.setRead(true);

        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);

        List<NotificationResponseDTO> responseDTOs = notificationService.getNotificationsByUserId(1L);

        assertEquals(2, responseDTOs.size());
        assertEquals("Test message 1", responseDTOs.get(0).getMessage());
        assertEquals(false, responseDTOs.get(0).isRead());
        assertEquals("Test message 2", responseDTOs.get(1).getMessage());
        assertEquals(true, responseDTOs.get(1).isRead());

        verify(notificationRepository, times(1)).findByUserId(1L);
    }
}

