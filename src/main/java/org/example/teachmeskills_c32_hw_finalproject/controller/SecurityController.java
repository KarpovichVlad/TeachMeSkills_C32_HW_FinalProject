package org.example.teachmeskills_c32_hw_finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.teachmeskills_c32_hw_finalproject.dto.security.AuthRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.security.AuthResponseDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.security.RegistrationRequestDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.user.UserDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.EmailUserException;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.LoginUsedException;
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
@Tag(name = "Security Controller", description = "Handles user registration and authentication")
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Login or email already in use")
    })
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

    @Operation(summary = "Generate JWT token for authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Token successfully generated"),
            @ApiResponse(responseCode = "401", description = "Invalid login or password")
    })
    @PostMapping("/token")
    public ResponseEntity<AuthResponseDto> generateToken(@RequestBody AuthRequestDto authRequestDto){
        Optional<String> token = securityService.generateToken(authRequestDto);
        if (token.isPresent()) {
            return new ResponseEntity<>(new AuthResponseDto(token.get()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}