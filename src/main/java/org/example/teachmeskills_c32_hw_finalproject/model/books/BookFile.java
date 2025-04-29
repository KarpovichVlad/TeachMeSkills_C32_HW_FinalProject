package org.example.teachmeskills_c32_hw_finalproject.model.books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "book_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFile {
    @Id
    @SequenceGenerator(name = "book_file_seq_gen", sequenceName = "book_files_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "book_file_seq_gen")
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "book_id", nullable = false, unique = true)
    private Long bookId;
}
