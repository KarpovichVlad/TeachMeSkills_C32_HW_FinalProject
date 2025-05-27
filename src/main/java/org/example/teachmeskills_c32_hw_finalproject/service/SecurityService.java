package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.security.AuthRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.security.RegistrationRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.EmailUserException;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.LoginUsedException;
import org.example.teachmeskills_c32_hw_finalproject.model.users.Role;
import org.example.teachmeskills_c32_hw_finalproject.model.users.Security;
import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.repository.SecurityRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.UserRepository;
import org.example.teachmeskills_c32_hw_finalproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SecurityService {

    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.securityRepository = securityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<UserDto> registration(RegistrationRequestDto requestDto) throws LoginUsedException, EmailUserException  {
        try {
            // Проверка на уникальность логина
            if (securityRepository.existsByLogin(requestDto.getLogin())) {
                throw new LoginUsedException(requestDto.getLogin());
            }

            // Проверка на уникальность email
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new EmailUserException(requestDto.getEmail());
            }
        User user = User.builder()
                .firstname(requestDto.getFirstname())
                .secondName(requestDto.getSecondName())
                .email(requestDto.getEmail())
                .age(requestDto.getAge())
                .sex(requestDto.getSex())
                .telephoneNumber(requestDto.getTelephoneNumber())
                .build();

        User savedUser = userRepository.save(user);

        // 2. Создание и сохранение Security
            Security security = Security.builder()
                    .login(requestDto.getLogin())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .role(Role.USER)
                    .userId(savedUser.getId())
                    .build();

        securityRepository.save(security);

        // 3. Преобразование в UserDto
        UserDto userDto = UserDto.builder()
                .id(savedUser.getId())
                .firstname(savedUser.getFirstname())
                .secondName(savedUser.getSecondName())
                .email(savedUser.getEmail())
                .age(savedUser.getAge())
                .sex(savedUser.getSex())
                .telephoneNumber(savedUser.getTelephoneNumber())
                .build();

        return Optional.of(userDto);
        } catch (DataIntegrityViolationException e) {
            // Исключение, если в базе данных есть нарушение уникальности
            if (e.getMessage().contains("users_email_key")) {
                throw new EmailUserException(requestDto.getEmail());
            } else {
                throw e;
            }
        }
    }

    public boolean canAccessUser(Long userId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Security> securityOptional = securityRepository.findByLogin(login);
        if (securityOptional.isEmpty()) {
            return false;
        }
        Security security = securityOptional.get();
        return security.getRole().equals(Role.ADMIN) || security.getUserId().equals(userId);
    }

    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        Optional<Security> securityOptional = securityRepository.findByLogin(authRequestDto.getLogin());

        if (securityOptional.isPresent() && passwordEncoder.matches(authRequestDto.getPassword(), securityOptional.get().getPassword())) {
            return jwtUtil.generateJwtToken(authRequestDto.getLogin());
        }
        return Optional.empty();
    }
}
