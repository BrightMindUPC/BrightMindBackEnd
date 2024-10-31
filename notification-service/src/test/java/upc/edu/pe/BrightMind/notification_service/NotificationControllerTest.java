package upc.edu.pe.BrightMind.notification_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import upc.edu.pe.BrightMind.notification_service.controller.NotificationController;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationRequestDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.NotificationResponseDTO;
import upc.edu.pe.BrightMind.notification_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.notification_service.service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateNotification() throws Exception {
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setMessage("Test message");
        requestDTO.setUserId(1L);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testUser");
        userResponseDTO.setEmail("test@example.com");

        NotificationResponseDTO responseDTO = new NotificationResponseDTO(
                1L, "Test message", false, 1L, LocalDateTime.now(), userResponseDTO);

        when(notificationService.createNotification(any(NotificationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Test message"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.read").value(false))
                .andExpect(jsonPath("$.user.username").value("testUser"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    public void testGetNotificationsByUserId() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testUser");
        userResponseDTO.setEmail("test@example.com");

        List<NotificationResponseDTO> notifications = Arrays.asList(
                new NotificationResponseDTO(1L, "Test message 1", false, 1L, LocalDateTime.now(), userResponseDTO),
                new NotificationResponseDTO(2L, "Test message 2", true, 1L, LocalDateTime.now(), userResponseDTO)
        );

        when(notificationService.getNotificationsByUserId(1L)).thenReturn(notifications);

        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].message").value("Test message 1"))
                .andExpect(jsonPath("$[0].user.username").value("testUser"))
                .andExpect(jsonPath("$[1].message").value("Test message 2"))
                .andExpect(jsonPath("$[1].user.email").value("test@example.com"));
    }

    @Test
    public void testMarkAsRead() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("testUser");
        userResponseDTO.setEmail("test@example.com");

        NotificationResponseDTO responseDTO = new NotificationResponseDTO(
                1L, "Test message", true, 1L, LocalDateTime.now(), userResponseDTO);

        when(notificationService.markAsRead(1L)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/notifications/1/mark-as-read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read").value(true))
                .andExpect(jsonPath("$.user.username").value("testUser"));
    }
}
