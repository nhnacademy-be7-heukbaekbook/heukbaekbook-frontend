package com.nhnacademy.heukbaekfrontend.review.client;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekfrontend.util.Utils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reviewClient", url = "http://localhost:8082/api/reviews")
public interface ReviewClient {

    @PostMapping
    ResponseEntity<ReviewCreateResponse> createReview(
            @RequestParam("orderId") String orderId,
            @RequestParam("bookId") Long bookId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("score") int score,
            @RequestBody(required = false) List<ReviewImageRequest> images
    );
    @GetMapping("/my")
    List<ReviewDetailResponse> getMyReviews();

}
