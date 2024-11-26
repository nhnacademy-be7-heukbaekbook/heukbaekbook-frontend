package com.nhnacademy.heukbaekfrontend.review.client;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reviewClient", url = "http://localhost:8082/api/reviews")
public interface ReviewClient {

    @GetMapping("/book/{bookId}")
    List<ReviewDetailResponse> getReviewsByBook(@PathVariable Long bookId);

    @GetMapping("/my")
    List<ReviewDetailResponse> getMyReviews(@RequestHeader("X-USER-ID") Long customerId);

    @PostMapping
    void createReview(@RequestHeader("X-USER-ID") Long customerId,
                      @ModelAttribute ReviewCreateRequest reviewCreateRequest);

    @PutMapping("/{orderId}/{bookId}")
    void updateReview(@RequestHeader("X-USER-ID") Long customerId,
                      @PathVariable Long orderId,
                      @PathVariable Long bookId,
                      @ModelAttribute ReviewUpdateRequest reviewUpdateRequest);
}
