package upc.edu.pe.BrightMind.user_service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserRequestDTO;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Para revertir cambios después de cada prueba
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUserIntegration() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUsername("integrationUser");
        requestDTO.setEmail("integration@example.com");
        requestDTO.setPassword("password");
        requestDTO.setRole(Role.TEACHER);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("integrationUser"))
                .andExpect(jsonPath("$.email").value("integration@example.com"))
                .andExpect(jsonPath("$.role").value("TEACHER"));
    }

    @Test
    public void testGetUserByIdIntegration() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUsername("integrationUser");
        requestDTO.setEmail("integration@example.com");
        requestDTO.setPassword("password");
        requestDTO.setRole(Role.TEACHER);

        String responseContent = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andReturn().getResponse().getContentAsString();

        UserResponseDTO createdUser = objectMapper.readValue(responseContent, UserResponseDTO.class);

        mockMvc.perform(get("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("integrationUser"))
                .andExpect(jsonPath("$.email").value("integration@example.com"))
                .andExpect(jsonPath("$.role").value("TEACHER"));
    }
}
