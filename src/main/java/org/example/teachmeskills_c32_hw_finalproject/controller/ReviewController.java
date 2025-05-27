package org.example.teachmeskills_c32_hw_finalproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/books/{bookId}/reviews")
@Tag(name = "Review Controller", description = "Manages book reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Get all reviews for a specific book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reviews returned successfully"),
            @ApiResponse(responseCode = "204", description = "No reviews found for this book")
    })
    @GetMapping("/list")
    public ResponseEntity<List<Review>> getAllReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @Operation(summary = "Create a new review for a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "409", description = "Review already exists or conflict occurred")
    })
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable Long bookId,
            @RequestBody @Valid Review review
    ) {
        review.setBookId(bookId);
        Optional<ReviewDto> createdReview = reviewService.createReview(review);

        if (createdReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview.get());
    }

    @Operation(summary = "Update an existing review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "409", description = "Could not update review due to conflict")
    })
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long bookId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateDto dto
    ) {
        Optional<ReviewResponseDto> updatedReview = reviewService.updateReview(bookId, reviewId, dto);
        if (updatedReview.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedReview.get(), HttpStatus.OK);
    }

    @Operation(summary = "Delete a review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "409", description = "Could not delete review due to conflict")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
        boolean deleted = reviewService.deleteReview(bookId, reviewId);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

