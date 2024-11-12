package com.nhnacademy.heukbaekfrontend.common.exception;

import java.util.Objects;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(Objects.isNull(message) ? "잘못된 접근입니다." : message);
    }
}
