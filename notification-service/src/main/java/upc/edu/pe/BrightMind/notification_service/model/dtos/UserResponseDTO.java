package upc.edu.pe.BrightMind.notification_service.model.dtos;

import lombok.Getter;
import lombok.Setter;
import upc.edu.pe.BrightMind.notification_service.model.dtos.enums.Department;
import upc.edu.pe.BrightMind.notification_service.model.dtos.enums.Grade;
import upc.edu.pe.BrightMind.notification_service.model.dtos.enums.Role;
import upc.edu.pe.BrightMind.notification_service.model.dtos.enums.Subject;


@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Role role;

    // Role-specific fields
    private Grade grade;          // For STUDENT
    private Subject subject;      // For TEACHER
    private Department department; // For ADMIN
}