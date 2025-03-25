package org.api.rest_api_grupo2.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.api.rest_api_grupo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
