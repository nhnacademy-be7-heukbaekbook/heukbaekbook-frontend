package com.nhnacademy.heukbaekfrontend.common.dto;

public record LoginResponse(
        Long userId,
        String userRole,
        String accessToken,
        String refreshToken,
        Long accessExpire,
        Long refreshExpire
) {
}
