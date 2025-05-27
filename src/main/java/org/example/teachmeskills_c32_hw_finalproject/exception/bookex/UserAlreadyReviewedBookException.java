package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class UserAlreadyReviewedBookException extends RuntimeException {
    public UserAlreadyReviewedBookException(Long userId, Long bookId) {
        super("A user with the ID " + userId + " has already left a review for a book with the ID  " + bookId);
    }
}
