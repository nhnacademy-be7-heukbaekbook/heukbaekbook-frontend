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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewClient reviewClient;

    @Override
    public List<ReviewDetailResponse> getMyReviews() {
        return reviewClient.getMyReviews().getBody();
    }

    @Override
    public ReviewDetailResponse getReviewDetails(Long bookId, String orderId) {
        return null;
    }

    @Override
    public void createReview(ReviewCreateRequest request) {
        reviewClient.createReview(request);
    }

    @Override
    public void updateReview(Long customerId, ReviewUpdateRequest request) {

    }


}
