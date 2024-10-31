package upc.edu.pe.BrightMind.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import upc.edu.pe.BrightMind.user_service.controller.UserController;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserRequestDTO;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;
import upc.edu.pe.BrightMind.user_service.service.UserService;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @Test
    public void testCreateUser() throws Exception {
        // Cubre la historia de usuario: US004 - Crear una Cuenta de Usuario Básica
        // Permite la creación de una cuenta de usuario en el sistema con sus datos básicos.
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUsername("testUser");
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setRole(Role.STUDENT);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("testUser");
        responseDTO.setEmail("test@example.com");
        responseDTO.setRole(Role.STUDENT);

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new IllegalArgumentException("User not found with id: 1"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found with id: 1"));
    }

}
