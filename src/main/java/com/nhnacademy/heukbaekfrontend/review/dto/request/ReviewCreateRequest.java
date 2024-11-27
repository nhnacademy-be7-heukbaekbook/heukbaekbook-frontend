package com.nhnacademy.heukbaekfrontend.review.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 리뷰 생성 요청 DTO
 */
public record ReviewCreateRequest(
        String orderId,
        Long bookId,
        String content,
        String title,
        int score,
        List<String> images
) {}
