package com.nhnacademy.heukbaekfrontend.review.controller;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookViewResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.review.dto.request.ReviewImageRequest;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewCreateResponse;
import com.nhnacademy.heukbaekfrontend.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.heukbaekfrontend.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/members/mypage/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookService bookService;

    /**
     * 리뷰 목록 조회
     */
    @GetMapping
    public String getReviews(Model model) {
        try {
            // 회원 등급 정보 가져오기
            GradeDto gradeDto = memberService.getMembersGrade().orElse(null);
            model.addAttribute("gradeDto", gradeDto);

            // 리뷰 목록 가져오기
            List<ReviewDetailResponse> reviews = reviewService.getMyReviews().stream()
                    .map(review -> {
                        // 리뷰와 관련된 책 정보 가져오기
                        BookViewResponse book = bookService.getBookDetailByBookId(review.bookId());
                        String bookTitle = book != null ? book.title() : "Unknown Book"; // 책 제목 설정

                        return new ReviewDetailResponse(
                                review.customerId(),
                                review.bookId(),
                                review.orderId(),
                                review.content(),
                                review.title(),
                                review.score(),
                                review.imageUrls(),
                                bookTitle                        );
                    })
                    .toList();

            if (reviews == null || reviews.isEmpty()) {
                log.warn("No reviews found for the current user");
                model.addAttribute("reviews", new ArrayList<>()); // 빈 리스트 추가
            } else {
                model.addAttribute("reviews", reviews);
            }

            return "review/reviewList"; // 리뷰 목록 뷰 템플릿으로 이동
        } catch (Exception e) {
            log.error("Failed to fetch reviews", e);
            model.addAttribute("error", "리뷰 목록을 가져오는 중 문제가 발생했습니다.");
            model.addAttribute("reviews", new ArrayList<>()); // 빈 리스트 추가
            return "review/reviewList";
        }
    }


    /**
     * 리뷰 작성
     */
    @PostMapping
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
            List<ReviewImageRequest> imageDataList = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String base64Data = java.util.Base64.getEncoder().encodeToString(image.getBytes());
                        imageDataList.add(new ReviewImageRequest(image.getOriginalFilename(), image.getContentType(), base64Data));
                    }
                }
            }

            ReviewCreateResponse response = reviewService.createReview(orderId, bookId, title, content, score, imageDataList);

//            if (response == null) {
//                log.warn("Review creation returned null response");
//                model.addAttribute("error", "리뷰 등록에 실패했습니다. 다시 시도해주세요.");
//            } else {
//                log.info("Review created successfully: {}", response);
//                model.addAttribute(
//                        "message", "리뷰가 성공적으로 등록되었습니다.");
//            }

            return "redirect:/members/mypage/reviews";
        } catch (Exception e) {
            log.error("Failed to create review", e);
            model.addAttribute("error", "리뷰 등록 중 문제가 발생했습니다.");
            return "redirect:/members/mypage/reviews";
        }
    }
}
