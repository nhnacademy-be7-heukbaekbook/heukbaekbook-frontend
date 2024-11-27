package com.nhnacademy.heukbaekfrontend.review.client;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "reviewClient", url = "http://localhost:8082/api/reviews")
public interface ReviewClient {
    @PostMapping
    ResponseEntity<ReviewDetailResponse> createReview(
            @RequestBody ReviewCreateRequest request);

    @PutMapping("{orderId}/{bookId}")
    ResponseEntity<Void> updateReview(
            @RequestHeader("X-USER-ID") Long customerId,
            @PathVariable String orderId,
            @PathVariable Long bookId,
            @ModelAttribute ReviewUpdateRequest request);
    @GetMapping("/book/{bookId}")
    ResponseEntity<List<ReviewDetailResponse>> getReviewsByBook(@PathVariable Long bookId);

    @GetMapping("/my")
    ResponseEntity<List<ReviewDetailResponse>> getMyReviews();


}
