package org.example.teachmeskills_c32_hw_finalproject.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDto {
    @NotBlank
    @Size(max = 1000)
    private String text;

    @Min(0)
    @Max(10)
    private double rating;
}
