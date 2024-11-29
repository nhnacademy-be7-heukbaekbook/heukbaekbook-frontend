package com.nhnacademy.heukbaekfrontend.couponset.coupon.client;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "couponClient", url = "http://localhost:8082/api/admin/coupons")
public interface CouponClient {

    @GetMapping
    ResponseEntity<Page<CouponResponse>> findAllNormalCoupons(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestParam(value = "sort", required = false) String sort
    );

    @GetMapping("/book-coupon")
    ResponseEntity<Page<BookCouponResponse>> findAllBookCoupons(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam(value = "sort", required = false) String sort
    );

    @GetMapping("/category-coupon")
    ResponseEntity<Page<CategoryCouponResponse>> findAllCategoryCoupons(@RequestParam("page") int page,
                                                                        @RequestParam("size") int size,
                                                                        @RequestParam(value = "sort", required = false) String sort
    );

    @PostMapping
    ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest couponRequest);

    @PutMapping("/{couponId}")
    ResponseEntity<CouponResponse> updateCoupon(@PathVariable Long couponId, @RequestBody CouponRequest couponRequest);

    @DeleteMapping("/{couponId}")
    ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId);

    @GetMapping("/coupon-page")
    ResponseEntity<CouponPageResponse> getCouponPageResponse(@RequestParam("page") int page,
                                                             @RequestParam("size") int size);
}
