package org.example.teachmeskills_c32_hw_finalproject.exception.userex;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("The user with the ID " + id + " was not found");
    }
}

