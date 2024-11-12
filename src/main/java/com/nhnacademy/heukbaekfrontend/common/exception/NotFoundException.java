package com.nhnacademy.heukbaekfrontend.common.exception;

import java.util.Objects;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(Objects.isNull(message) ? "데이터가 없습니다." : message);
    }
}
