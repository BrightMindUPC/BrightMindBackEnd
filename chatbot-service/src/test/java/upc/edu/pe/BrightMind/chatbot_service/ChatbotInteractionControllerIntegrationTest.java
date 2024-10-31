package upc.edu.pe.BrightMind.chatbot_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.ChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.CreateChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.User;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.enums.Role;
import upc.edu.pe.BrightMind.chatbot_service.repository.UserRepository;
import upc.edu.pe.BrightMind.chatbot_service.service.ChatbotInteractionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChatbotInteractionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatbotInteractionService chatbotInteractionService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        // Elimina todas las interacciones para evitar conflictos de claves foráneas.
        chatbotInteractionService.getAllInteractions().forEach(interaction -> chatbotInteractionService.deleteInteraction(interaction.getId()));

        // Elimina todos los usuarios de la base de datos.
        userRepository.deleteAll();

        // Crea un usuario con todos los campos obligatorios
        user = new User("TestUser", "testuser@example.com", "securePassword123", Role.STUDENT);
        user = userRepository.save(user); // Guarda el usuario en la base de datos
    }

    @Test
    public void testCreateInteraction() throws Exception {
        CreateChatbotInteractionDTO dto = new CreateChatbotInteractionDTO(user.getId(), "Hello, bot!", "Hello, user!");

        mockMvc.perform(post("/api/chatbot-interactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userMessage").value("Hello, bot!"))
                .andExpect(jsonPath("$.botResponse").value("Hello, user!"));
    }

    @Test
    public void testGetInteractionById() throws Exception {
        ChatbotInteractionDTO createdInteraction = chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "Hello", "Hi there!"));

        mockMvc.perform(get("/api/chatbot-interactions/" + createdInteraction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMessage").value("Hello"))
                .andExpect(jsonPath("$.botResponse").value("Hi there!"));
    }

    @Test
    public void testGetAllInteractions() throws Exception {
        chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "Hello", "Hi there!"));
        chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "How are you?", "I'm good!"));

        mockMvc.perform(get("/api/chatbot-interactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testGetInteractionsByUserId() throws Exception {
        chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "Hello", "Hi there!"));
        chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "How are you?", "I'm good!"));

        mockMvc.perform(get("/api/chatbot-interactions/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testUpdateInteraction() throws Exception {
        ChatbotInteractionDTO createdInteraction = chatbotInteractionService.createInteraction(new CreateChatbotInteractionDTO(user.getId(), "Hello", "Hi there!"));

        CreateChatbotInteractionDTO updateDto = new CreateChatbotInteractionDTO(user.getId(), "Updated message", "Updated response");

        mockMvc.perform(put("/api/chatbot-interactions/" + createdInteraction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userMessage").value("Updated message"))
                .andExpect(jsonPath("$.botResponse").value("Updated response"));
    }
}
