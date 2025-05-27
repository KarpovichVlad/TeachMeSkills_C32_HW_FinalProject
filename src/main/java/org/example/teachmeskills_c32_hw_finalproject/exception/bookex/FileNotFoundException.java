package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String fileName) {
        super("File " + fileName + " not found.");
    }
}
