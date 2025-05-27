package org.example.teachmeskills_c32_hw_finalproject.exception;

import io.jsonwebtoken.JwtException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.FileNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.GenreNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.ReviewAlreadyExistsException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.ReviewNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.UserAlreadyReviewedBookException;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.EmailUserException;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.LoginUsedException;
import org.example.teachmeskills_c32_hw_finalproject.exception.userex.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Map<String, Object> generateResponse(String message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "message", message
        );
    }

    @ExceptionHandler(LoginUsedException.class)
    public ResponseEntity<?> loginUsedExceptionHandler(LoginUsedException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailUserException.class)
    public ResponseEntity<?> emailUserExceptionHandler(EmailUserException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(BookNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<?> handleGenreNotFound(GenreNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFound(FileNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<?> handleReviewNotFound(ReviewNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<?> handleReviewAlreadyExists(ReviewAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedExceptionHandler(AccessDeniedException exception) {
        log.warn("Access is denied: {}", exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(UserAlreadyReviewedBookException.class)
    public ResponseEntity<?> userAlreadyReviewedBookHandler(UserAlreadyReviewedBookException exception) {
        log.warn("Repeated review rejected: {}", exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<?> jwtExceptionHandler(JwtException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(generateResponse(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // Прочие исключения
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> defaultExceptionHandler(Exception exception) {
        log.error("Unexpected error: {}", exception.getMessage(), exception);
        return new ResponseEntity<>(generateResponse("An unexpected error has occurred. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
