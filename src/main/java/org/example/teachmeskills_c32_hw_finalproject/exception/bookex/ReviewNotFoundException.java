package org.example.teachmeskills_c32_hw_finalproject.exception.bookex;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long reviewId) {
        super("Отзыв с ID " + reviewId + " не найден");
    }
}