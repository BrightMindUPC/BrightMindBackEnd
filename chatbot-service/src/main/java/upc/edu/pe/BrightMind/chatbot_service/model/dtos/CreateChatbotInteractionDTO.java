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
}

