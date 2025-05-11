package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя с ID {}: {}", id, e.getMessage());
            return false;
        }
        return !userRepository.existsById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

