package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.UserNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    public Optional<User> getUserById(Long id) {
        if (!securityService.canAccessUser(id)) {
            throw new AccessDeniedException("Access denied by user ID: " + id);
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
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

    @Transactional
    public Optional<UserDto> updateUser(Long id, UserUpdateDto dto) {
        if (!securityService.canAccessUser(id)) {
            throw new AccessDeniedException("Access denied by user ID: " + id);
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            log.warn("The user with the ID {} was not found for updating", id);
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
            log.error("Error when updating the user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (!securityService.canAccessUser(id)) {
            throw new AccessDeniedException("Access denied by user ID: " + id);
        }
        if (!userRepository.existsById(id)) {
            log.warn("The user with ID {} was not found for deletion", id);
            throw new UserNotFoundException(id);
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error when deleting a user with an ID {}: {}", id, e.getMessage());
            return false;
        }
        return !userRepository.existsById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

