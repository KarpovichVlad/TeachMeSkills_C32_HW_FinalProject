package org.example.teachmeskills_c32_hw_finalproject.repository;

import org.example.teachmeskills_c32_hw_finalproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
