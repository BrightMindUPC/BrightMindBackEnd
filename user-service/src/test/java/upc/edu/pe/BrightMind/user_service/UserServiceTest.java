package upc.edu.pe.BrightMind.user_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserRequestDTO;
import upc.edu.pe.BrightMind.user_service.model.dtos.UserResponseDTO;
import upc.edu.pe.BrightMind.user_service.model.entities.User;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;
import upc.edu.pe.BrightMind.user_service.repository.UserRepository;
import upc.edu.pe.BrightMind.user_service.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUsername("testUser");
        requestDTO.setEmail("test@example.com");
        requestDTO.setPassword("password");
        requestDTO.setRole(Role.STUDENT);

        User user = new User(requestDTO.getUsername(), requestDTO.getEmail(), requestDTO.getPassword(), requestDTO.getRole());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.createUser(requestDTO);

        assertEquals("testUser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(Role.STUDENT, response.getRole());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testUpdateUser_Success() {
        User existingUser = new User("existingUser", "existing@example.com", "password", Role.STUDENT);
        existingUser.setId(1L);

        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setPassword("newpassword");
        updateDTO.setRole(Role.TEACHER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDTO response = userService.updateUser(1L, updateDTO);

        assertEquals("updatedUser", response.getUsername());
        assertEquals("updated@example.com", response.getEmail());
        assertEquals(Role.TEACHER, response.getRole());
    }
}