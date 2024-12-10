package com.nhnacademy.heukbaekfrontend.point.dto;

import lombok.Getter;

@Getter
public enum PointType {
    EARNED("+", "적립"),
    USED("-", "사용"),
    CANCELLED("-", "취소");

    private final String prefix;
    private final String description;

    PointType(String prefix, String description) {
        this.prefix = prefix;
        this.description = description;
    }

}
