package org.example.teachmeskills_c32_hw_finalproject.service;

import org.example.teachmeskills_c32_hw_finalproject.model.books.Book;
import org.example.teachmeskills_c32_hw_finalproject.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Boolean createBook(Book book) {
        try{
            bookRepository.save(book);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public Optional<Book> updateBook(Book book) {
        return Optional.of(bookRepository.save(book));
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return !bookRepository.existsById(id);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }
}
