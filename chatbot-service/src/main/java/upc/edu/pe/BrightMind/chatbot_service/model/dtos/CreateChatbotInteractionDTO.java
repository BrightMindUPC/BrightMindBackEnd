package upc.edu.pe.BrightMind.chatbot_service.model.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatbotInteractionDTO {

    @NotNull
    private Long userId;

    @NotBlank
    private String userMessage;

    @NotBlank
    private String botResponse;

    // Constructor con parámetros
    public CreateChatbotInteractionDTO(Long userId, String userMessage, String botResponse) {
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
    }

    // Constructor vacío (necesario para serialización/deserialización)
    public CreateChatbotInteractionDTO() {}
}
