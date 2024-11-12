package com.nhnacademy.heukbaekfrontend.common.exception;

import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(Objects.isNull(message) ? "잘못된 요청입니다." : message);
    }
}
