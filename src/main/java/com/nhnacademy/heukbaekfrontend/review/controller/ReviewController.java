package com.nhnacademy.heukbaekfrontend.review.controller;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/mypage")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 폼 이동
    @GetMapping("/review")
    public String getReviewForm(@RequestParam Long bookId,
                                @RequestParam String orderId,
                                Model model) {
        log.info("Received orderId: {}", orderId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("orderId", orderId);
        return "review/reviewForm";
    }
    @PostMapping("/reviews")
    public String createReview(@RequestParam String orderId,
                               @RequestParam Long bookId,
                               @RequestParam String title,
                               @RequestParam String content,
                               @RequestParam int score,
                               @RequestParam(required = false) List<MultipartFile> images,
                               Model model) {
        try {
            // Authentication에서 사용자 ID 추출
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            Long customerId;
            if (principal instanceof Long) {
                customerId = (Long) principal; // Long인 경우 그대로 사용
            } else if (principal instanceof String) {
                customerId = Long.valueOf((String) principal); // String이면 Long으로 변환
            } else {
                throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
            }

            log.info("Creating review for customerId: {}", customerId);

            // 이미지 처리
            List<String> imageBase64List = images != null
                    ? images.stream()
                    .map(image -> {
                        try {
                            return Base64.getEncoder().encodeToString(image.getBytes());
                        } catch (Exception e) {
                            log.error("Image encoding failed", e);
                            return null;
                        }
                    })
                    .collect(Collectors.toList())
                    : List.of();

            // 리뷰 생성 요청
            ReviewCreateRequest reviewRequest = new ReviewCreateRequest(orderId, bookId, content, title, score, imageBase64List);
            reviewService.createReview(reviewRequest);

            model.addAttribute("message", "리뷰가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            log.error("리뷰 작성 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "리뷰 작성 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "review/reviewForm";
        }
        return "redirect:/members/mypage/reviews";
    }



    // 리뷰 목록
    @GetMapping("/reviews")
    public String listMyReviews(Model model) {
        model.addAttribute("reviews", reviewService.getMyReviews());
        log.info(model.toString());
        return "review/reviewList";
    }
}
