package com.nhnacademy.heukbaekfrontend.review.dto.response;

import com.nhnacademy.heukbaekfrontend.book.dto.response.BookResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookViewResponse;

import java.util.List;

/**
 * 리뷰 상세 조회 응답 DTO
 */
public record ReviewDetailResponse(
        Long customerId,
        Long bookId,
        String orderId,
        String content,
        String title,
        int score,
        List<String> imageUrls,
        String bookTitle
) {}
