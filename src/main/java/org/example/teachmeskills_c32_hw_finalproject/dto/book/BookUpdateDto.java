package org.example.teachmeskills_c32_hw_finalproject.dto.book;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class BookUpdateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genre;

    @Size(max = 1000)
    private String description;

    @Min(value = 0)
    private Integer releaseYear;
}
