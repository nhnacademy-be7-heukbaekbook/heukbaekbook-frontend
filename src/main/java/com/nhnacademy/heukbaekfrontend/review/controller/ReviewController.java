package com.nhnacademy.heukbaekfrontend.review.controller;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/members/mypage")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public String createReview(
            @RequestParam("orderId") String orderId,
            @RequestParam("bookId") Long bookId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("score") int score,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            Model model
    ) {
        try {
            // Multipart 데이터를 Base64 문자열로 변환
            List<ReviewImageRequest> imageDataList = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        // Base64 변환
                        String base64Data = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                        imageDataList.add(new ReviewImageRequest(image.getOriginalFilename(), image.getContentType(), base64Data));
                    }
                }
            }

            // Service로 데이터 전달
            ReviewCreateResponse response = reviewService.createReview(orderId, bookId, title, content, score, imageDataList);

            if (response == null) {
                log.warn("Review creation returned null response");
                model.addAttribute("error", "리뷰 등록에 실패했습니다. 다시 시도해주세요.");
            } else {
                log.info("Review created successfully: {}", response);
                model.addAttribute("message", "리뷰가 성공적으로 등록되었습니다.");
            }

            // 리뷰 목록 가져오기
            List<ReviewDetailResponse> reviews = reviewService.getMyReviews();
            if (reviews == null || reviews.isEmpty()) {
                log.warn("No reviews found for the current user");
                model.addAttribute("reviews", new ArrayList<>()); // 빈 리스트 추가
            } else {
                model.addAttribute("reviews", reviews);
            }

            return "review/reviewList"; // 리뷰 목록 뷰 템플릿으로 이동
        } catch (Exception e) {
            log.error("Failed to create review", e);

            // 에러 발생 시 기존 리뷰 목록 유지
            List<ReviewDetailResponse> reviews = reviewService.getMyReviews();
            if (reviews == null || reviews.isEmpty()) {
                model.addAttribute("reviews", new ArrayList<>()); // 빈 리스트 추가
            } else {
                model.addAttribute("reviews", reviews);
            }
            model.addAttribute("error", "리뷰 등록 중 문제가 발생했습니다.");

            return "review/reviewList"; // 리뷰 목록 뷰 템플릿으로 이동
        }
    }
}
