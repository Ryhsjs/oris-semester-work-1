package ru.itis.flavorful_book.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.exception.IllegalUserArgumentException;
import ru.itis.flavorful_book.form.SignupForm;
import ru.itis.flavorful_book.repository.UserRepository;
import ru.itis.flavorful_book.security.CustomeUserDetails;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(SignupForm form) {
        IllegalUserArgumentException ex = new IllegalUserArgumentException("Ошибка при регистрации");

        if (userRepository.existsByUsername(form.getUsername())) {
            ex.setUsernameState("Это имя пользователя уже используется");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            ex.setEmailState("Этот email уже используется");
        }
        if (ex.isShouldThrow()) {
            throw ex;
        }

        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(Long id, String username, String avatarUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + id + " не найден"));

        validateUsername(username, user.getUsername());

        user.setUsername(username);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + id + " не найден"));
    }

    private void validateUsername(String newUsername, String currentUsername) {
        if (!newUsername.equals(currentUsername) && userRepository.existsByUsername(newUsername)) {
            IllegalUserArgumentException ex = new IllegalUserArgumentException("Ошибка в имени пользователя");
            ex.setUsernameState("Это имя пользователя уже используется");
            throw ex;
        }
    }

    @Override
    public CustomeUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return new CustomeUserDetails(user);
    }
}
