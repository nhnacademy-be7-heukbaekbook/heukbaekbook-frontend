package com.nhnacademy.heukbaekfrontend.book.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record BookCreateResponse(
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        boolean isPackable,
        int stock,
        int standardPrice,
        BigDecimal discountRate,
        String publisher,
        List<String> categories,
        List<String> authors
) {}