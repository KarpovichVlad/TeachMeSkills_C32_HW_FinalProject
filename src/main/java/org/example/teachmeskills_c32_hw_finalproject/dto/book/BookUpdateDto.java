package org.example.teachmeskills_c32_hw_finalproject.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Size(max = 100)
    private String genre;

    @Size(max = 1000)
    private String description;

    @Min(value = 0)
    private Integer releaseYear;
}
