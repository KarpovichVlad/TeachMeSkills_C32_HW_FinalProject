package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewResponseDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewUpdateDto;
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

    public Optional<ReviewDto> createReview(Review review) {
        try {
            Review saved = reviewRepository.save(review);

            return Optional.of(ReviewDto.builder()
                    .id(saved.getId())
                    .text(saved.getText())
                    .rating(saved.getRating())
                    .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<ReviewResponseDto> updateReview(Long bookId, Long reviewId, ReviewUpdateDto dto) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty() || !reviewOpt.get().getBookId().equals(bookId)) {
            return Optional.empty();
        }

        Review review = reviewOpt.get();
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

