package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(Long bookId) {
        super("A review for a book with the ID " + bookId + " already exists");
    }
}

