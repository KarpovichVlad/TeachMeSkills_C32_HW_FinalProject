package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.example.teachmeskills_c32_hw_finalproject.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public boolean createReview(Review review) {
        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public Optional<Review> updateReview(Review review) {
        try {
            return Optional.of(reviewRepository.save(review));
        } catch (Exception e) {
            log.error("Ошибка при обновлении отзыва: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteReview(Long reviewId) {
        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            log.error("Ошибка при удалении отзыва с ID {}: {}", reviewId, e.getMessage());
            return false;
        }
        return !reviewRepository.existsById(reviewId);
    }

    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }
}

