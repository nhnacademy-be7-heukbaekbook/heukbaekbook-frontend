package com.nhnacademy.heukbaekfrontend.review.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekfrontend.review.client.ReviewClient;
import com.nhnacademy.heukbaekfrontend.review.dto.ReviewDto;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewService {

    @Autowired
    private ReviewClient reviewClient;

    @Autowired
    private ObjectMapper objectMapper;

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
        log.info("client 전");
        return reviewClient.getMyReviews();
    }
}
