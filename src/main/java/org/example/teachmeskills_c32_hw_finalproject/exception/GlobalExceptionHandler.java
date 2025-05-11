package org.example.teachmeskills_c32_hw_finalproject.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = LoginUsedException.class)
    public ResponseEntity<String> loginUsedExceptionHandler(LoginUsedException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmailUserException.class)
    public ResponseEntity<String> emailUserExceptionHandler(EmailUserException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    // Прочие исключения
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultExceptionHandler(Exception exception) {
        log.error("Unexpected error: " + exception.getMessage());
        return new ResponseEntity<>("Произошла ошибка. Пожалуйста, повторите попытку позже.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
