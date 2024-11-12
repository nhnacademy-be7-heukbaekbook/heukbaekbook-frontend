package com.nhnacademy.heukbaekfrontend.common.exception;

import java.util.Objects;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(Objects.isNull(message) ? "유효한 인증 자격이 없습니다." : message);
    }
}
