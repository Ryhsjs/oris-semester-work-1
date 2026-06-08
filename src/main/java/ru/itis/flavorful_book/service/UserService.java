package ru.itis.flavorful_book.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.form.SignupForm;

public interface UserService extends UserDetailsService {
    void register(SignupForm form);

    void update(Long id, String username, String avatarUrl);

    User findById(Long id);
}
