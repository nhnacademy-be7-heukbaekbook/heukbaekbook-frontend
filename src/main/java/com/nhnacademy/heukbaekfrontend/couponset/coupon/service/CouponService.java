package com.nhnacademy.heukbaekfrontend.couponset.coupon.service;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CouponService {

    Page<CouponResponse> getAllNormalCoupons(Pageable pageable);
    Page<BookCouponResponse> getAllBookCoupons(Pageable pageable);
    Page<CategoryCouponResponse> getAllCategoryCoupons(Pageable pageable);
    Optional<CouponResponse> createCoupon(CouponRequest couponRequest);
    Optional<CouponResponse> updateCoupon(Long couponId, CouponRequest couponRequest);
    void deleteCoupon(Long couponId);
}
