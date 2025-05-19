package org.example.teachmeskills_c32_hw_finalproject.repository;

import org.example.teachmeskills_c32_hw_finalproject.model.books.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenreIgnoreCase(String genre);
}
