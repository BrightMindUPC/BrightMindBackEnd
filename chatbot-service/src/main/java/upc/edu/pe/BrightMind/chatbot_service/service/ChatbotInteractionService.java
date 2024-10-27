package upc.edu.pe.BrightMind.chatbot_service.service;

import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.ChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.dtos.CreateChatbotInteractionDTO;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.ChatbotInteraction;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.User;
import upc.edu.pe.BrightMind.chatbot_service.repository.ChatbotInteractionRepository;
import upc.edu.pe.BrightMind.chatbot_service.repository.UserRepository;
import upc.edu.pe.BrightMind.chatbot_service.service.util.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotInteractionService {

    private final ChatbotInteractionRepository chatbotInteractionRepository;
    private final UserRepository userRepository;

    public ChatbotInteractionService(ChatbotInteractionRepository chatbotInteractionRepository,
                                     UserRepository userRepository) {
        this.chatbotInteractionRepository = chatbotInteractionRepository;
        this.userRepository = userRepository;
    }

    public ChatbotInteractionDTO createInteraction(CreateChatbotInteractionDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ChatbotInteraction interaction = new ChatbotInteraction();
        interaction.setUser(user);
        interaction.setUserMessage(dto.getUserMessage());
        interaction.setBotResponse(dto.getBotResponse());
        interaction.setTimestamp(LocalDateTime.now());

        ChatbotInteraction savedInteraction = chatbotInteractionRepository.save(interaction);

        return mapToDTO(savedInteraction);
    }

    public ChatbotInteractionDTO getInteractionById(Long id) {
        ChatbotInteraction interaction = chatbotInteractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        return mapToDTO(interaction);
    }

    public List<ChatbotInteractionDTO> getInteractionsByUserId(Long userId) {
        List<ChatbotInteraction> interactions = chatbotInteractionRepository.findByUserId(userId);
        return interactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ChatbotInteractionDTO> getAllInteractions() {
        List<ChatbotInteraction> interactions = chatbotInteractionRepository.findAll();
        return interactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ChatbotInteractionDTO updateInteraction(Long id, CreateChatbotInteractionDTO dto) {
        ChatbotInteraction interaction = chatbotInteractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interaction not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        interaction.setUser(user);
        interaction.setUserMessage(dto.getUserMessage());
        interaction.setBotResponse(dto.getBotResponse());

        ChatbotInteraction updatedInteraction = chatbotInteractionRepository.save(interaction);

        return mapToDTO(updatedInteraction);
    }

    public void deleteInteraction(Long id) {
        if (!chatbotInteractionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interaction not found");
        }
        chatbotInteractionRepository.deleteById(id);
    }

    private ChatbotInteractionDTO mapToDTO(ChatbotInteraction interaction) {
        ChatbotInteractionDTO dto = new ChatbotInteractionDTO();
        dto.setId(interaction.getId());
        dto.setUserId(interaction.getUser().getId());
        dto.setUserMessage(interaction.getUserMessage());
        dto.setBotResponse(interaction.getBotResponse());
        dto.setTimestamp(interaction.getTimestamp());
        return dto;
    }
}
