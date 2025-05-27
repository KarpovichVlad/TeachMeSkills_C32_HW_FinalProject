package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String genre) {
        super("Genre " + genre + " not found in the database.");
    }
}