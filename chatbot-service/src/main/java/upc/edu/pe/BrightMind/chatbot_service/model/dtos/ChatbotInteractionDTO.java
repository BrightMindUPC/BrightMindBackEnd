package upc.edu.pe.BrightMind.chatbot_service.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatbotInteractionDTO {

    private Long id;
    private Long userId;
    private String userMessage;
    private String botResponse;
    private LocalDateTime timestamp;
}

