package upc.edu.pe.BrightMind.chatbot_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.ChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.CreateChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.ChatbotInteraction;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.User;
import upc.edu.pe.BrightMind.chatbot_service.repository.ChatbotInteractionRepository;
import upc.edu.pe.BrightMind.chatbot_service.repository.UserRepository;
import upc.edu.pe.BrightMind.chatbot_service.service.ChatbotInteractionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatbotInteractionServiceTest {

    @Mock
    private ChatbotInteractionRepository chatbotInteractionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatbotInteractionService chatbotInteractionService;

    private User user;


    @BeforeEach
    public void setup() {
        // Inicializa el objeto usuario con un ID válido
        user = new User();
        user.setId(1L);
    }

    @Test
    public void testCreateInteraction() {
        // Cubre la historia de usuario: US011 - Consultar Dudas Académicas en Tiempo Real
        ChatbotInteraction interaction = new ChatbotInteraction();
        interaction.setId(1L);
        interaction.setUser(user);  // Configura el usuario aquí
        interaction.setUserMessage("Hello, bot!");
        interaction.setBotResponse("Hello, user!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(chatbotInteractionRepository.save(any(ChatbotInteraction.class))).thenReturn(interaction);

        CreateChatbotInteractionDTO dto = new CreateChatbotInteractionDTO(1L, "Hello, bot!", "Hello, user!");
        ChatbotInteractionDTO result = chatbotInteractionService.createInteraction(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserMessage()).isEqualTo("Hello, bot!");
        assertThat(result.getBotResponse()).isEqualTo("Hello, user!");
    }

    @Test
    public void testGetInteractionById() {
        ChatbotInteraction interaction = new ChatbotInteraction();
        interaction.setId(1L);
        interaction.setUser(user);  // Configura el usuario aquí
        interaction.setUserMessage("Hello, bot!");
        interaction.setBotResponse("Hello, user!");

        when(chatbotInteractionRepository.findById(1L)).thenReturn(Optional.of(interaction));

        ChatbotInteractionDTO result = chatbotInteractionService.getInteractionById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserMessage()).isEqualTo("Hello, bot!");
        assertThat(result.getBotResponse()).isEqualTo("Hello, user!");
    }

    @Test
    public void testGetAllInteractions() {
        ChatbotInteraction interaction1 = new ChatbotInteraction();
        interaction1.setId(1L);
        interaction1.setUser(user);  // Configura el usuario aquí
        interaction1.setUserMessage("Message 1");
        interaction1.setBotResponse("Response 1");

        ChatbotInteraction interaction2 = new ChatbotInteraction();
        interaction2.setId(2L);
        interaction2.setUser(user);  // Configura el usuario aquí
        interaction2.setUserMessage("Message 2");
        interaction2.setBotResponse("Response 2");

        when(chatbotInteractionRepository.findAll()).thenReturn(Arrays.asList(interaction1, interaction2));

        List<ChatbotInteractionDTO> result = chatbotInteractionService.getAllInteractions();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getUserMessage()).isEqualTo("Message 1");
        assertThat(result.get(1).getUserMessage()).isEqualTo("Message 2");
    }

    @Test
    public void testDeleteInteraction() {
        when(chatbotInteractionRepository.existsById(1L)).thenReturn(true);

        chatbotInteractionService.deleteInteraction(1L);

        verify(chatbotInteractionRepository, times(1)).deleteById(1L);
    }
}

