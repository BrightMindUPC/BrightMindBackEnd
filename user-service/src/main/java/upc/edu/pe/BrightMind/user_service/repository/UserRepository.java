package upc.edu.pe.BrightMind.user_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upc.edu.pe.BrightMind.user_service.model.entities.User;
import upc.edu.pe.BrightMind.user_service.model.entities.enums.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos existentes
    List<User> findByRole(Role role);

    // Nuevos métodos
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
