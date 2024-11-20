package com.nhnacademy.heukbaekfrontend.book.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

public record BookCreateRequest(
        String title,
        String index,
        String description,
        String publishedAt,
        String isbn,
        String imageUrl,
        Boolean isPackable,
        @Min(0) int stock,
        @Min(0) int standardPrice,
        @DecimalMin(value = "0.0")float discountRate,
        String publisher,
        String categories,
        String authors
) {}