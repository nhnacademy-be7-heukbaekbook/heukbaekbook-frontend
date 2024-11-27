package com.nhnacademy.heukbaekfrontend.review.service;


import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;

import java.util.List;

public interface ReviewService {

    ReviewDetailResponse getReviewDetails(Long bookId, String orderId);

    void createReview(ReviewCreateRequest request);

    void updateReview(Long customerId, ReviewUpdateRequest request);
    List<ReviewDetailResponse> getMyReviews();

    List<ReviewDetailResponse> getReviewsByBook(Long bookId);
}
