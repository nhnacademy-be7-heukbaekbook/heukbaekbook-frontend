package com.nhnacademy.heukbaekfrontend.review.service;

import com.nhnacademy.heukbaekfrontend.review.client.ReviewClient;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewClient reviewClient;

    public ReviewCreateResponse createReview(String orderId, Long bookId, String title, String content, int score, List<ReviewImageRequest> images) {
//        List<MultipartFile> validImages = new ArrayList<>();
//        if (images != null && !images.isEmpty()) {
//            for (MultipartFile file : images) {
//                if (!file.isEmpty()) {
//                    validImages.add(file);
//                }
//            }
//        }
        return reviewClient.createReview(orderId, bookId, title, content, score, images).getBody();
    }

    public List<ReviewDetailResponse> getMyReviews() {
        return reviewClient.getMyReviews();
    }

    public List<ReviewDetailResponse> getMyReviewsByBook(Long bookId) {
        return reviewClient.getReviewsByBook(bookId);
    }

    public boolean hasReview(Long orderId, Long bookId) {
        return  reviewClient.hasReview(orderId,bookId);
    }
    public List<ReviewDetailResponse> getMyReviewsByOrder(String orderId) {
        return reviewClient.getReviewsByOrder(orderId);
    }


}
