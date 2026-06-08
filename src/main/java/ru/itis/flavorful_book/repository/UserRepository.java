package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.flavorful_book.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
