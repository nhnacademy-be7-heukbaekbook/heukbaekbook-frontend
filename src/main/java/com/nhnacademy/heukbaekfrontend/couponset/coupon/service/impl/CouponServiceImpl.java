package com.nhnacademy.heukbaekfrontend.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.client.CouponClient;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponClient couponClient;


    @Override
    public Page<CouponResponse> getAllNormalCoupons(Pageable pageable) {
        ResponseEntity<Page<CouponResponse>> coupons = couponClient.findAllNormalCoupons(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "couponCreatedAt, desc");
        return coupons.getBody();
    }

    @Override
    public Page<BookCouponResponse> getAllBookCoupons(Pageable pageable) {
        ResponseEntity<Page<BookCouponResponse>> coupons = couponClient.findAllBookCoupons(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "couponCreatedAt, desc");
        return coupons.getBody();
    }

    @Override
    public Page<CategoryCouponResponse> getAllCategoryCoupons(Pageable pageable) {
        ResponseEntity<Page<CategoryCouponResponse>> coupons = couponClient.findAllCategoryCoupons(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "couponCreatedAt, desc");
        return coupons.getBody();
    }

    @Override
    public Optional<CouponResponse> createCoupon(CouponRequest couponRequest) {
        ResponseEntity<CouponResponse> responseEntity = couponClient.createCoupon(couponRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<CouponResponse> updateCoupon(Long couponId, CouponRequest couponRequest) {
        ResponseEntity<CouponResponse> responseEntity = couponClient.updateCoupon(couponId, couponRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void deleteCoupon(Long couponId) {
        couponClient.deleteCoupon(couponId);
    }

    @Override
    public CouponPageResponse getCouponPageResponse(Pageable pageable) {
        return couponClient.getCouponPageResponse(pageable.getPageNumber(), pageable.getPageSize()).getBody();
    }
}
