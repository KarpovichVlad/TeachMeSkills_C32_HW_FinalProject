package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.annotation.LogExecutionTime;
import org.example.teachmeskills_c32_hw_finalproject.dto.book.BookCreateDto;
import org.example.teachmeskills_c32_hw_finalproject.dto.book.BookUpdateDto;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.BookNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.exception.bookex.GenreNotFoundException;
import org.example.teachmeskills_c32_hw_finalproject.model.books.Book;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Optional<Book> createBook(BookCreateDto dto) {
        try {
            Book book = Book.builder()
                    .title(dto.getTitle())
                    .author(dto.getAuthor())
                    .genre(dto.getGenre())
                    .description(dto.getDescription())
                    .releaseYear(dto.getReleaseYear())
                    .build();

            return Optional.of(bookRepository.save(book));
        } catch (Exception e) {
            log.error("Error when creating a book: {}", e.getMessage());
            return Optional.empty();
        }
    }


    @LogExecutionTime
    @Transactional
    public Optional<Book> updateBook(Long id, BookUpdateDto dto) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            log.warn("The book with the ID {} could not be found for updating", id);
            throw new BookNotFoundException(id);
        }

        Book book = bookOpt.get();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setDescription(dto.getDescription());
        book.setReleaseYear(dto.getReleaseYear());

        try {
            Book updated = bookRepository.save(book);
            return Optional.of(updated);
        } catch (Exception e) {
            log.error("Error updating the book: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Book> getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            log.warn("Book with ID {} not found", id);
            throw new BookNotFoundException(id);
        }
        return book;
    }

    @Transactional
    public boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            log.warn("The book with the ID {} could not be found for deletion", id);
            throw new BookNotFoundException(id);
        }
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error when deleting a book with an ID {}: {}", id, e.getMessage());
            return false;
        }
        return !bookRepository.existsById(id);
    }

    @LogExecutionTime
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByGenre(String genre) {
        List<Book> books = bookRepository.findByGenreIgnoreCase(genre);
        if (books.isEmpty()) {
            log.warn("Genre not found: {}", genre);
            throw new GenreNotFoundException(genre);
        }
        return books;
    }
}
