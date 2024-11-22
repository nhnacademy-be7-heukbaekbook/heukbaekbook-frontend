package com.nhnacademy.heukbaekfrontend.couponset.coupon.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponStatus;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponController {
    private final CouponService couponService;
    private final CouponPolicyService couponPolicyService;
    private static final String REDIRECT_PATH = "redirect:/admin/coupons";

    @Admin
    @ModelAttribute
    public void before(Model model, Pageable pageable) {
        Page<CouponResponse> normalCoupons = couponService.getAllNormalCoupons(pageable);
        Page<BookCouponResponse> bookCoupons = couponService.getAllBookCoupons(pageable);
        Page<CategoryCouponResponse> categoryCoupons = couponService.getAllCategoryCoupons(pageable);
        List<CouponPolicyResponse> couponPolicyList = couponPolicyService.getCouponPolicyList();

        model.addAttribute("normalCoupons", normalCoupons);
        model.addAttribute("bookCoupons", bookCoupons);
        model.addAttribute("categoryCoupons", categoryCoupons);
        model.addAttribute("discountType", DiscountType.values());
        model.addAttribute("status", CouponStatus.values());
        model.addAttribute("couponPolicyList", couponPolicyList);
    }

    @Admin
    @GetMapping
    public String getCouponPage() {
        return "coupon/admin/coupon";
    }

    @Admin
    @PostMapping
    public String addCoupon(@Valid @ModelAttribute CouponRequest couponRequest,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("couponRequest", couponRequest);
            return REDIRECT_PATH;
        }
        couponService.createCoupon(couponRequest);
        return REDIRECT_PATH;
    }

    @Admin
    @PutMapping("/{couponId}")
    public String updateCoupon(@PathVariable Long couponId, @ModelAttribute CouponRequest couponRequest) {
        couponService.updateCoupon(couponId, couponRequest);
        return REDIRECT_PATH;
    }

    @Admin
    @DeleteMapping("/{couponId}")
    public String deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return REDIRECT_PATH;
    }
}








