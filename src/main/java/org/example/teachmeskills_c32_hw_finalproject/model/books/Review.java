package org.example.teachmeskills_c32_hw_finalproject.model.books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "book_id"})
})
public class Review {
    @Id
    @SequenceGenerator(name = "reviews_seq_gen", sequenceName = "reviews_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "reviews_seq_gen")
    private Long id;

    @Column(length = 2000)
    private String text;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", updatable = false)
    private Date createdAt;
}

