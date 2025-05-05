package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.example.teachmeskills_c32_hw_finalproject.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public boolean createReview(Review review) {
        try {
            reviewRepository.save(review);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Review> updateReview(Review review) {
        return Optional.of(reviewRepository.save(review));
    }

    public boolean deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findAllByBookId(bookId);
    }
}
