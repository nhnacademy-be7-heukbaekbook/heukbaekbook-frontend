package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record CouponPolicyResponse(
        Long id,
        DiscountType discountType,
        BigDecimal discountAmount,
        BigDecimal minimumPurchaseAmount,
        BigDecimal maximumPurchaseAmount
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}