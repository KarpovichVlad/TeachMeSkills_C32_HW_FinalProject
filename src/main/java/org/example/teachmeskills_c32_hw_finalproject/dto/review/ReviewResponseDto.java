package org.example.teachmeskills_c32_hw_finalproject.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {
    private String text;
    private double rating;
}
