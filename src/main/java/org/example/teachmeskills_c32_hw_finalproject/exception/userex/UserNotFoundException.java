package org.example.teachmeskills_c32_hw_finalproject.exception.userex;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Пользователь с ID " + id + " не найден");
    }
}

