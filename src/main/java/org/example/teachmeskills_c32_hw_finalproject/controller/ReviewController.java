package org.example.teachmeskills_c32_hw_finalproject.controller;

import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewResponseDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.example.teachmeskills_c32_hw_finalproject.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Review>> getAllReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Long bookId,
            @RequestBody Review review
    ) {
        review.setBookId(bookId);
        Optional<ReviewDto> createdReview = reviewService.createReview(review);

        if (createdReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview.get());
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateDto dto
    ) {
        Optional<ReviewResponseDto> updatedReview = reviewService.updateReview(bookId, reviewId, dto);
        if (updatedReview.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedReview.get(), HttpStatus.OK);
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long reviewId) {
        boolean deleted = reviewService.deleteReview(reviewId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

