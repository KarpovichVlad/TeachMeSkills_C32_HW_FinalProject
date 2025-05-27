package org.example.teachmeskills_c32_hw_finalproject.repository;

import org.example.teachmeskills_c32_hw_finalproject.model.books.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByBookId(Long bookId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

}
