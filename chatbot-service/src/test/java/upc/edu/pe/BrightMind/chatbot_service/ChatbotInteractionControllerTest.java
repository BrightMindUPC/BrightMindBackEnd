package upc.edu.pe.BrightMind.chatbot_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import upc.edu.pe.BrightMind.chatbot_service.controller.ChatbotInteractionController;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.ChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.CreateChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.service.ChatbotInteractionService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatbotInteractionController.class)
public class ChatbotInteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatbotInteractionService chatbotInteractionService;

    @Test
    public void testCreateInteraction() throws Exception {
        // Cubre la historia de usuario: US011 - Consultar Dudas Académicas en Tiempo Real
        CreateChatbotInteractionDTO dto = new CreateChatbotInteractionDTO(1L, "Hello, bot!", "Hello, user!");
        ChatbotInteractionDTO responseDTO = new ChatbotInteractionDTO();
        responseDTO.setId(1L);
        responseDTO.setUserId(1L);
        responseDTO.setUserMessage("Hello, bot!");
        responseDTO.setBotResponse("Hello, user!");

        when(chatbotInteractionService.createInteraction(any(CreateChatbotInteractionDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/chatbot-interactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"userMessage\":\"Hello, bot!\",\"botResponse\":\"Hello, user!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userMessage").value("Hello, bot!"))
                .andExpect(jsonPath("$.botResponse").value("Hello, user!"));
    }

    @Test
    public void testGetInteractionById() throws Exception {
        ChatbotInteractionDTO responseDTO = new ChatbotInteractionDTO();
        responseDTO.setId(1L);
        responseDTO.setUserId(1L);
        responseDTO.setUserMessage("Hello, bot!");
        responseDTO.setBotResponse("Hello, user!");

        when(chatbotInteractionService.getInteractionById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/chatbot-interactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userMessage").value("Hello, bot!"))
                .andExpect(jsonPath("$.botResponse").value("Hello, user!"));
    }

    @Test
    public void testGetAllInteractions() throws Exception {
        ChatbotInteractionDTO interaction1 = new ChatbotInteractionDTO();
        interaction1.setId(1L);
        interaction1.setUserMessage("Message 1");
        interaction1.setBotResponse("Response 1");

        ChatbotInteractionDTO interaction2 = new ChatbotInteractionDTO();
        interaction2.setId(2L);
        interaction2.setUserMessage("Message 2");
        interaction2.setBotResponse("Response 2");

        when(chatbotInteractionService.getAllInteractions()).thenReturn(Arrays.asList(interaction1, interaction2));

        mockMvc.perform(get("/api/chatbot-interactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userMessage").value("Message 1"))
                .andExpect(jsonPath("$[1].userMessage").value("Message 2"));
    }

    @Test
    public void testDeleteInteraction() throws Exception {
        mockMvc.perform(delete("/api/chatbot-interactions/1"))
                .andExpect(status().isNoContent());

        verify(chatbotInteractionService, times(1)).deleteInteraction(1L);
    }
}

