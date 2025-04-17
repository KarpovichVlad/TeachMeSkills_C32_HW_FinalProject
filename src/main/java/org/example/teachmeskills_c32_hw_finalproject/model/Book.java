package org.example.teachmeskills_c32_hw_finalproject.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class Book {
    @Id
    @SequenceGenerator(name = "book_seq_gen", sequenceName = "books_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "book_seq_gen")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String genre;

    @Column(length = 1000)
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;
}


