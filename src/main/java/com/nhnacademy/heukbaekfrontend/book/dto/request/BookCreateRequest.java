package com.nhnacademy.heukbaekfrontend.book.dto.request;

public record BookCreateRequest(
        String title,
        String index,
        String description,
        String publication,
        String isbn,
        String imageUrl,
        Boolean isPackable,
        int stock,
        int standardPrice,
        float discountRate,
        String publisher,
        String categories,
        String authors
) {}