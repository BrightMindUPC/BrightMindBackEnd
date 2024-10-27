package upc.edu.pe.BrightMind.user_service.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserRequestDTO;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.user_service.model.entities.User;
import upc.edu.pe.BrightMind.user_service.model.entities.details.AdminDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.details.StudentDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.details.TeacherDetails;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;
import upc.edu.pe.BrightMind.user_service.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        // Verificar si el username o email ya existen
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRole());

        // Manejo de detalles específicos del rol
        switch (dto.getRole()) {
            case STUDENT:
                StudentDetails studentDetails = new StudentDetails(dto.getGrade(), user);
                user.setStudentDetails(studentDetails);
                break;
            case TEACHER:
                TeacherDetails teacherDetails = new TeacherDetails(dto.getSubject(), user);
                user.setTeacherDetails(teacherDetails);
                break;
            case ADMIN:
                AdminDetails adminDetails = new AdminDetails(dto.getDepartment(), user);
                user.setAdminDetails(adminDetails);
                break;
            default:
                throw new IllegalArgumentException("Rol especificado no válido");
        }

        try {
            User savedUser = userRepository.save(user);
            return convertToResponseDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El username o email ya existe");
        }
    }

    public UserResponseDTO getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return convertToResponseDTO(optionalUser.get());
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("No se encontró usuario con el id: " + id);
        }
        User user = optionalUser.get();

        // Verificar si el nuevo username o email ya están en uso por otro usuario
        if (!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        boolean roleChanged = !user.getRole().equals(dto.getRole());

        // Actualizar campos básicos del usuario
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        user.setRole(dto.getRole());

        if (roleChanged) {
            // Eliminar detalles antiguos
            if (user.getStudentDetails() != null) {
                user.setStudentDetails(null);
            }
            if (user.getTeacherDetails() != null) {
                user.setTeacherDetails(null);
            }
            if (user.getAdminDetails() != null) {
                user.setAdminDetails(null);
            }
        }

        // Manejo de detalles específicos del rol
        switch (dto.getRole()) {
            case STUDENT:
                StudentDetails studentDetails = user.getStudentDetails();
                if (studentDetails == null) {
                    studentDetails = new StudentDetails();
                    studentDetails.setUser(user);
                    user.setStudentDetails(studentDetails);
                }
                studentDetails.setGrade(dto.getGrade());
                break;
            case TEACHER:
                TeacherDetails teacherDetails = user.getTeacherDetails();
                if (teacherDetails == null) {
                    teacherDetails = new TeacherDetails();
                    teacherDetails.setUser(user);
                    user.setTeacherDetails(teacherDetails);
                }
                teacherDetails.setSubject(dto.getSubject());
                break;
            case ADMIN:
                AdminDetails adminDetails = user.getAdminDetails();
                if (adminDetails == null) {
                    adminDetails = new AdminDetails();
                    adminDetails.setUser(user);
                    user.setAdminDetails(adminDetails);
                }
                adminDetails.setDepartment(dto.getDepartment());
                break;
            default:
                throw new IllegalArgumentException("Rol especificado no válido");
        }

        try {
            User updatedUser = userRepository.save(user);
            return convertToResponseDTO(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El username o email ya existe");
        }
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<UserResponseDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert User entity to UserResponseDTO
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());

        switch (user.getRole()) {
            case STUDENT:
                if (user.getStudentDetails() != null) {
                    responseDTO.setGrade(user.getStudentDetails().getGrade());
                }
                break;
            case TEACHER:
                if (user.getTeacherDetails() != null) {
                    responseDTO.setSubject(user.getTeacherDetails().getSubject());
                }
                break;
            case ADMIN:
                if (user.getAdminDetails() != null) {
                    responseDTO.setDepartment(user.getAdminDetails().getDepartment());
                }
                break;
            default:
                break;
        }
        return responseDTO;
    }

    public UserResponseDTO getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("No se encontró usuario con el username: " + username);
        }
        return convertToResponseDTO(optionalUser.get());
    }

    public UserResponseDTO getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("No se encontró usuario con el email: " + email);
        }
        return convertToResponseDTO(optionalUser.get());
    }
}
