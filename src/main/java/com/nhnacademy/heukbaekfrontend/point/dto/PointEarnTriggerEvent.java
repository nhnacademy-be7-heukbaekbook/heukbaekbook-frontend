package com.nhnacademy.heukbaekfrontend.point.dto;

import lombok.Getter;

@Getter
public enum PointEarnTriggerEvent {
    LOGIN("로그인"),
    SIGNUP("회원가입"),
    ORDER("주문"),
    BASIC_EARNING_RATE("기본적립률"),
    REVIEW_WITH_PHOTO("사진 포함 리뷰"),
    REVIEW_WITHOUT_PHOTO("사진 없는 리뷰"),
    EVENT("이벤트 참여");


    private final String displayName;

    PointEarnTriggerEvent(String displayName) {
        this.displayName = displayName;
    }

}
