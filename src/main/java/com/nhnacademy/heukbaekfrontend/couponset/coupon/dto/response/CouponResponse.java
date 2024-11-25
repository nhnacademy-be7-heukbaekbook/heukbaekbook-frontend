package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response;


import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;

import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        CouponPolicyResponse couponPolicyResponse,
        String couponStatus,
        int availableDuration,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd,
        String couponName,
        String couponDescription,
        LocalDateTime couponCreatedAt
) {
}