package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.annotation.LogExecutionTime;
import org.example.teachmeskills_c32_hw_finalproject.model.books.Book;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean createBook(Book book) {
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            log.error("Ошибка при создании книги: {}", e.getMessage());
            return false;
        }
        return true;
    }

    @LogExecutionTime
    public Optional<Book> updateBook(Book book) {
        try {
            return Optional.of(bookRepository.save(book));
        } catch (Exception e) {
            log.error("Ошибка при обновлении книги: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public boolean deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Ошибка при удалении книги с ID {}: {}", id, e.getMessage());
            return false;
        }
        return !bookRepository.existsById(id);
    }

    @LogExecutionTime
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
