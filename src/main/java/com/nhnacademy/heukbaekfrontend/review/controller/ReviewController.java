package com.nhnacademy.heukbaekfrontend.review.controller;

import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewCreateRequest;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // 리뷰 작성 처리
    @PostMapping("/reviews")
    public String createReview(@RequestParam String orderId,
                               @RequestParam Long bookId,
                               @RequestParam String title,
                               @RequestParam String content,
                               @RequestParam int score,
                               @RequestParam(required = false) List<MultipartFile> images,
                               Model model) {
        try {
            ReviewCreateRequest reviewRequest = new ReviewCreateRequest(orderId, bookId, content, title, score, images);
            reviewService.createReview(reviewRequest); // customerId는 서비스 내부에서 처리
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
