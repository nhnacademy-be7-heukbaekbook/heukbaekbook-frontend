package com.nhnacademy.heukbaekfrontend.common.dto;

import java.io.Serial;
import java.io.Serializable;

public record LoginResponse(
        Long userId,
        String userRole,
        String accessToken,
        String refreshToken,
        Long accessExpire,
        Long refreshExpire
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
