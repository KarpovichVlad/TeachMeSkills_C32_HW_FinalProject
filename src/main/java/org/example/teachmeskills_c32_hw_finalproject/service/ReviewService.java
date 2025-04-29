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

    public Boolean createReview(Review review) {
        try{
            reviewRepository.save(review);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public Optional<Review> updateReview(Review review) {
        return Optional.of(reviewRepository.save(review));
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Boolean deleteReview(Long id) {
        reviewRepository.deleteById(id);
        return !reviewRepository.existsById(id);
    }

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
}
