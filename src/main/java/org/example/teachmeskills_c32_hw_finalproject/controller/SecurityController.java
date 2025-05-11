package org.example.teachmeskills_c32_hw_finalproject.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.teachmeskills_c32_hw_finalproject.dto.securiy.RegistrationRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.EmailUserException;
import org.example.teachmeskills_c32_hw_finalproject.exception.LoginUsedException;
import org.example.teachmeskills_c32_hw_finalproject.model.users.User;
import org.example.teachmeskills_c32_hw_finalproject.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/security")
@Tag(name = "Security Controller", description = "Управление регистрацией")
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody @Valid RegistrationRequestDto requestDto, BindingResult bindingResult) throws LoginUsedException, EmailUserException {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<UserDto> userDto = securityService.registration(requestDto);
        if (userDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userDto.get(), HttpStatus.CREATED);
    }
}