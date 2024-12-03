package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponHistoryResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.MemberCouponResponse;
import org.springframework.data.domain.Page;

public interface CouponPageService {
    Page<MemberCouponResponse> getAvailableCoupons();
    Page<CouponHistoryResponse> getUsedCoupons();
}
