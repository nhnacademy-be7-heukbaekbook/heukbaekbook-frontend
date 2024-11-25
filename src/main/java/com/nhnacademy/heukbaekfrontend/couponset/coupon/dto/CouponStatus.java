package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto;

import lombok.Getter;

import java.util.List;

@Getter
public enum CouponStatus {
    ABLE("사용가능"),
    DISABLE("만료됨");


    private final String value;

    CouponStatus(String value) {
        this.value = value;
    }

}
