package org.example.teachmeskills_c32_hw_finalproject.controller;

import org.example.teachmeskills_c32_hw_finalproject.service.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book/{id}/review")
public class ReviewController {

    public ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

}
