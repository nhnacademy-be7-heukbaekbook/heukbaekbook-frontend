package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response;


import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;

import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        CouponPolicyResponse couponPolicyResponse,
        String couponStatus,
        Integer couponQuantity,
        int availableDuration,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd,
        CouponType couponType,

        String couponName,
        String couponDescription,
        LocalDateTime couponCreatedAt
) {
}