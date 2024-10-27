package upc.edu.pe.BrightMind.user_service.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Department;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Grade;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Subject;

@Getter
@Setter
public class UserRequestDTO {

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "El email es requerido")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // Role-specific fields
    private Grade grade;          // For STUDENT
    private Subject subject;      // For TEACHER
    private Department department; // For ADMIN
}
