package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto;

import lombok.Getter;

@Getter
public enum CouponType {
    BIRTHDAY("생일 쿠폰"),
    WELCOME("웰컴 쿠폰"),
    BOOK("도서 쿠폰"),
    CATEGORY("카테고리 쿠폰"),
    GENERAL("일반 쿠폰");

    private final String displayName;

    CouponType(String displayName) {
        this.displayName = displayName;
    }
}
