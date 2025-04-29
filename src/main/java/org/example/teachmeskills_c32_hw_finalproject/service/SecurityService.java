package org.example.teachmeskills_c32_hw_finalproject.service;


import org.example.teachmeskills_c32_hw_finalproject.model.dto.RegistrationRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.model.users.Role;
import org.example.teachmeskills_c32_hw_finalproject.model.users.Security;
import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.repository.SecurityRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SecurityService {
    private User user;
    private Security security;
    public final SecurityRepository securityRepository;
    private final UserRepository userRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, User user, Security security, UserRepository userRepository) {
        this.securityRepository = securityRepository;
        this.user = user;
        this.security = security;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> registration(RegistrationRequestDto requestDto) {
        user.setFirstname(requestDto.getFirstname());
        user.setSecondName(requestDto.getSecondName());
        user.setEmail(requestDto.getEmail());
        user.setAge(requestDto.getAge());
        user.setSex(requestDto.getSex());
        user.setTelephoneNumber(requestDto.getTelephoneNumber());
        User userUpdated = userRepository.save(user);

        security.setLogin(requestDto.getLogin());
        security.setPassword(requestDto.getPassword());
        security.setRole(Role.USER);
        security.setUserId(userUpdated.getId());
        securityRepository.save(security);
        return userRepository.findById(userUpdated.getId());
    }
}
