package upc.edu.pe.BrightMind.chatbot_service.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.chatbot_service.model.entities.util.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_interactions")
@Getter
@Setter
public class ChatbotInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que realizó la interacción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Mensaje enviado por el usuario
    @Column(nullable = false)
    private String userMessage;

    // Respuesta del chatbot
    @Column(nullable = false)
    private String botResponse;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors
    public ChatbotInteraction() {}

    public ChatbotInteraction(User user, String userMessage, String botResponse, LocalDateTime timestamp) {
        this.user = user;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.timestamp = timestamp;
    }
}

