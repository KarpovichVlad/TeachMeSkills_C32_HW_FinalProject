package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String genre) {
        super("Жанр " + genre + " не найден в базе данных.");
    }
}