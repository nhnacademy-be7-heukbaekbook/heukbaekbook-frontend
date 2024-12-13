package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.client.MemberCouponClient;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponHistoryResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.MemberCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.CouponPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponPageServiceImpl implements CouponPageService {
    private final MemberCouponClient memberCouponClient;

    @Override
    public Page<MemberCouponResponse> getAvailableCoupons() {
        return memberCouponClient.getAvailableCoupons();
    }

    @Override
    public Page<CouponHistoryResponse> getUsedCoupons() {
        return memberCouponClient.getUsedCoupons();
    }
}
