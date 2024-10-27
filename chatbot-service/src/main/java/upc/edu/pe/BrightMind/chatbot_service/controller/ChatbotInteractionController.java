package upc.edu.pe.BrightMind.chatbot_service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.ChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.CreateChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.service.ChatbotInteractionService;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot-interactions")
public class ChatbotInteractionController {

    private final ChatbotInteractionService chatbotInteractionService;

    public ChatbotInteractionController(ChatbotInteractionService chatbotInteractionService) {
        this.chatbotInteractionService = chatbotInteractionService;
    }

    @PostMapping
    public ResponseEntity<ChatbotInteractionDTO> createInteraction(@Valid @RequestBody CreateChatbotInteractionDTO dto) {
        ChatbotInteractionDTO createdInteraction = chatbotInteractionService.createInteraction(dto);
        return new ResponseEntity<>(createdInteraction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatbotInteractionDTO> getInteractionById(@PathVariable Long id) {
        ChatbotInteractionDTO interaction = chatbotInteractionService.getInteractionById(id);
        return ResponseEntity.ok(interaction);
    }

    @GetMapping
    public ResponseEntity<List<ChatbotInteractionDTO>> getAllInteractions() {
        List<ChatbotInteractionDTO> interactions = chatbotInteractionService.getAllInteractions();
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatbotInteractionDTO>> getInteractionsByUserId(@PathVariable Long userId) {
        List<ChatbotInteractionDTO> interactions = chatbotInteractionService.getInteractionsByUserId(userId);
        return ResponseEntity.ok(interactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatbotInteractionDTO> updateInteraction(@PathVariable Long id,
                                                                   @Valid @RequestBody CreateChatbotInteractionDTO dto) {
        ChatbotInteractionDTO updatedInteraction = chatbotInteractionService.updateInteraction(id, dto);
        return ResponseEntity.ok(updatedInteraction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Long id) {
        chatbotInteractionService.deleteInteraction(id);
        return ResponseEntity.noContent().build();
    }
}
