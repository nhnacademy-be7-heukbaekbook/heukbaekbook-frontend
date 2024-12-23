package com.nhnacademy.heukbaekfrontend.book.dto.response;

import java.util.List;

public record BookUpdateResponse(
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        String imageUrl,
        boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String bookStatus,
        String publisher,
        List<String> categories,
        List<String> authors
) {}