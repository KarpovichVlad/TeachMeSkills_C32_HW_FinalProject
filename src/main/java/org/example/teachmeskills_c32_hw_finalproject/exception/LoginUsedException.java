package org.example.teachmeskills_c32_hw_finalproject.exception;


public class LoginUsedException extends Exception {
    String login;

    public LoginUsedException(String login) {
        super("Login already used: " + login);
    }

    @Override
    public String toString() {
        return "Login already used: " + login;
    }
}