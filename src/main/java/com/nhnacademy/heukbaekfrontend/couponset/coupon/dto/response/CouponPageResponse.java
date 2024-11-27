package com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponStatus;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record CouponPageResponse (
        Page<CouponResponse> normalCoupons,
        Page<BookCouponResponse> bookCoupons,
        Page<CategoryCouponResponse> categoryCoupons,
        List<CouponPolicyResponse> couponPolicyList,
        DiscountType[] discountType,
        CouponStatus[] couponstatus,
        CouponType[] couponType
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}