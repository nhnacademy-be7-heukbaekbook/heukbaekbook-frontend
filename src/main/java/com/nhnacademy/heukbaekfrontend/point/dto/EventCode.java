package com.nhnacademy.heukbaekfrontend.point.dto;

import lombok.Getter;

@Getter
public enum EventCode {
    LOGIN(1L, "로그인"),
    SIGNUP(2L, "회원가입"),
    ORDER(3L, "주문"),
    REVIEW_WITH_PHOTO(4L, "사진 포함 리뷰"),
    REVIEW_WITHOUT_PHOTO(5L, "사진 없는 리뷰"),
    EVENT(6L, "이벤트 참여");

    private final Long id;
    private final String displayName;

    EventCode(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
}
