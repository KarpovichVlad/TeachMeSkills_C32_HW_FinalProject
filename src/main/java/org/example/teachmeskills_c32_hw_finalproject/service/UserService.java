package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.UserNotFoundException;
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

    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("Пользователь с ID {} не найден", id);
            throw new UserNotFoundException(id);
        }
        return user;
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .secondName(user.getSecondName())
                .age(user.getAge())
                .sex(user.getSex())
                .email(user.getEmail())
                .telephoneNumber(user.getTelephoneNumber())
                .build();
    }

    public Optional<UserDto> updateUser(Long id, UserUpdateDto dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            log.warn("Пользователь с ID {} не найден для обновления", id);
            throw new UserNotFoundException(id);
        }

        User user = userOpt.get();
        user.setFirstname(dto.getFirstname());
        user.setSecondName(dto.getSecondName());
        user.setAge(dto.getAge());
        user.setSex(dto.getSex());
        user.setTelephoneNumber(dto.getTelephoneNumber());

        try {
            User updated = userRepository.save(user);
            return Optional.of(convertToDto(updated));
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("Пользователь с ID {} не найден для удаления", id);
            throw new UserNotFoundException(id);
        }
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

