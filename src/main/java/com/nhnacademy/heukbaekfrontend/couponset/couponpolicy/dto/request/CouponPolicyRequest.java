package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CouponPolicyRequest(
        @NotNull
        DiscountType discountType,

        @NotNull
        BigDecimal discountAmount,

        @NotNull
        BigDecimal minimumPurchaseAmount,

        BigDecimal maximumPurchaseAmount
) {
}
