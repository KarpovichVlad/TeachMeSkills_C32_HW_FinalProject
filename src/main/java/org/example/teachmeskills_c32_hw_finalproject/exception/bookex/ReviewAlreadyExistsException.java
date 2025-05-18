package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException(Long bookId) {
        super("Отзыв для книги с ID " + bookId + " уже существует");
    }
}

