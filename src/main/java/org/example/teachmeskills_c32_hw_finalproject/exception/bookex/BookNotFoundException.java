package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Книга с ID " + id + " не найдена");
    }
}

