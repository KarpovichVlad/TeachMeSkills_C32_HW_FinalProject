package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewResponseDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.review.ReviewUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.ReviewNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.UserAlreadyReviewedBookException;
import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.example.teachmeskills_c32_hw_finalproject.model.users.Security;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.ReviewRepository;
import org.example.teachmeskills_c32_hw_finalproject.repository.SecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final SecurityRepository securityRepository;
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, SecurityRepository securityRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.securityRepository = securityRepository;
    }

    @Transactional
    public Optional<ReviewDto> createReview(Review review) {
        if (!bookRepository.existsById(review.getBookId())) {
            log.warn("Ошибка при попытке создать отзыв для несуществующей книги с ID {}", review.getBookId());
            throw new BookNotFoundException(review.getBookId());
        }

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Security> securityOptional = securityRepository.findByLogin(login);

        if (securityOptional.isEmpty()) {
            log.error("Пользователь с логином {} не найден в базе security", login);
            return Optional.empty();
        }

        Long userId = securityOptional.get().getUserId();
        review.setUserId(userId);

        if (reviewRepository.existsByUserIdAndBookId(userId, review.getBookId())) {
            log.warn("Пользователь с ID {} уже оставил отзыв на книгу с ID {}", userId, review.getBookId());
            throw new UserAlreadyReviewedBookException(userId, review.getBookId());
        }

        try {
            Review saved = reviewRepository.save(review);

            return Optional.of(ReviewDto.builder()
                    .id(saved.getId())
                    .text(saved.getText())
                    .rating(saved.getRating())
                    .build());
        } catch (Exception e) {
            log.error("Ошибка при создании отзыва: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }


    @Transactional
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

    @Transactional
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
