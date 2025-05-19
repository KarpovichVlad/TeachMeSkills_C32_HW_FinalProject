package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewResponseDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.ReviewNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public Optional<ReviewDto> createReview(Review review) {
        if (!bookRepository.existsById(review.getBookId())) {
            log.warn("Ошибка при попытки создать отзыв для несуществующей книги с ID {}", review.getBookId());
            throw new BookNotFoundException(review.getBookId());
        }

        try {
            Review saved = reviewRepository.save(review);

            return Optional.of(ReviewDto.builder()
                    .id(saved.getId())
                    .text(saved.getText())
                    .rating(saved.getRating())
                    .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<ReviewResponseDto> updateReview(Long bookId, Long reviewId, ReviewUpdateDto dto) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            log.error("Отзыв с ID {} не найден при попытке обновления", reviewId);
            throw new ReviewNotFoundException(reviewId);
        }

        Review review = reviewOpt.get();
        if (!review.getBookId().equals(bookId)) {
            log.error("Книга с ID {} не соответствует отзыву с ID {}", bookId, reviewId);
            throw new BookNotFoundException(bookId);
        }

        review.setText(dto.getText());
        review.setRating(dto.getRating());

        try {
            Review updated = reviewRepository.save(review);
            return Optional.of(
                    ReviewResponseDto.builder()
                            .text(updated.getText())
                            .rating(updated.getRating())
                            .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при обновлении отзыва с ID {}: {}", reviewId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean deleteReview(Long bookId, Long reviewId) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            log.error("Попытка удалить несуществующий отзыв с ID {}", reviewId);
            throw new ReviewNotFoundException(reviewId);
        }

        Review review = reviewOpt.get();
        if (!review.getBookId().equals(bookId)) {
            log.error("Отзыв с ID {} не принадлежит книге с ID {}", reviewId, bookId);
            throw new ReviewNotFoundException(reviewId);
        }

        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            log.error("Ошибка при удалении отзыва с ID {}: {}", reviewId, e.getMessage(), e);
            return false;
        }
        return !reviewRepository.existsById(reviewId);
    }

    public List<Review> getReviewsByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            log.error("Попытка получить отзывы для несуществующей книги с ID {}", bookId);
            throw new BookNotFoundException(bookId);
        }
        return reviewRepository.findAllByBookId(bookId);
    }
}
