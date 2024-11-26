package com.nhnacademy.heukbaekfrontend.review.service;

import com.nhnacademy.heukbaekfrontend.review.client.ReviewClient;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewUpdateRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewClient reviewClient;

    public List<ReviewDetailResponse> getReviewsByBook(Long bookId) {
        return reviewClient.getReviewsByBook(bookId);
    }

    public List<ReviewDetailResponse> getMyReviews(Long customerId) {
        return reviewClient.getMyReviews(customerId);
    }

    public void createReview(Long customerId, ReviewCreateRequest reviewCreateRequest) {
        reviewClient.createReview(customerId, reviewCreateRequest);
    }

    public void updateReview(Long customerId, Long orderId, Long bookId, ReviewUpdateRequest reviewUpdateRequest) {
        reviewClient.updateReview(customerId, orderId, bookId, reviewUpdateRequest);
    }
}
