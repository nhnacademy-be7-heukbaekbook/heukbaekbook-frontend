package com.nhnacademy.heukbaekfrontend.review.controller;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/book/{bookId}")
    public ModelAndView getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewDetailResponse> reviews = reviewService.getReviewsByBook(bookId);
        return new ModelAndView("reviews/review-list").addObject("reviews", reviews);
    }

    @GetMapping("/my")
    public ModelAndView getMyReviews(HttpSession session) {
        Long customerId = (Long) session.getAttribute("customerId");
        List<ReviewDetailResponse> reviews = reviewService.getMyReviews(customerId);
        return new ModelAndView("reviews/my-reviews").addObject("reviews", reviews);
    }

    @PostMapping
    public ResponseEntity<Void> createReview(HttpSession session,
                                             @ModelAttribute ReviewCreateRequest reviewCreateRequest) {
        Long customerId = (Long) session.getAttribute("customerId");
        reviewService.createReview(customerId, reviewCreateRequest);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{orderId}/{bookId}")
    public ResponseEntity<Void> updateReview(HttpSession session,
                                             @PathVariable Long orderId,
                                             @PathVariable Long bookId,
                                             @ModelAttribute ReviewUpdateRequest reviewUpdateRequest) {
        Long customerId = (Long) session.getAttribute("customerId");
        reviewService.updateReview(customerId, orderId, bookId, reviewUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
