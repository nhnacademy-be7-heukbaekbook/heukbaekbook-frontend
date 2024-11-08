package com.nhnacademy.heukbaekfrontend.category.dto.response;

public record CategoryDetailResponse(
        Long id,
        Long parentId,
        String name
) {}
