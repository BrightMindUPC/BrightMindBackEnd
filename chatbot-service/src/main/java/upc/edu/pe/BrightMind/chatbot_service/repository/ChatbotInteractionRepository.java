package upc.edu.pe.BrightMind.chatbot_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.ChatbotInteraction;

import java.util.List;

public interface ChatbotInteractionRepository extends JpaRepository<ChatbotInteraction, Long> {

    List<ChatbotInteraction> findByUserId(Long userId);
}

