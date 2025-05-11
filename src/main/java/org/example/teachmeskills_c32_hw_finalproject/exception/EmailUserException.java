package org.example.teachmeskills_c32_hw_finalproject.exception;

public class EmailUserException  extends RuntimeException{
    public EmailUserException(String email) {
        super("Email already exists: " + email);
    }
}
