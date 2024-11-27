package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponStatus;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CategoryCouponResponse (
        Long couponId,
        String couponName,
        String couponDescription,
        LocalDateTime couponCreatedAt,
        CouponStatus couponStatus,
        int availableDuration,
        LocalDateTime couponTimeStart,
        LocalDateTime couponTimeEnd,

        Long policyId,
        DiscountType discountType,
        BigDecimal discountAmount,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumPurchaseAmount,

        Long categoryId,
        String name
){
}
