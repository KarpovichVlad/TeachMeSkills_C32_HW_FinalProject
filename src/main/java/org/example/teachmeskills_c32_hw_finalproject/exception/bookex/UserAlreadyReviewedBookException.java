package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class UserAlreadyReviewedBookException extends RuntimeException {
    public UserAlreadyReviewedBookException(Long userId, Long bookId) {
        super("Пользователь с ID " + userId + " уже оставил отзыв на книгу с ID " + bookId);
    }
}
