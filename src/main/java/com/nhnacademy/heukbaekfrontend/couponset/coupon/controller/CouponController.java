package com.nhnacademy.heukbaekfrontend.couponset.coupon.controller;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponPageResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
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
    private final MemberService memberService;
    private final CouponService couponService;
    private final CouponPolicyService couponPolicyService;
    private static final String REDIRECT_PATH = "redirect:/admin/coupons";

//    @ModelAttribute
//    public void before(Model model, Pageable pageable) {
//        Page<CouponResponse> normalCoupons = couponService.getAllNormalCoupons(pageable);
//        Page<BookCouponResponse> bookCoupons = couponService.getAllBookCoupons(pageable);
//        Page<CategoryCouponResponse> categoryCoupons = couponService.getAllCategoryCoupons(pageable);
//        List<CouponPolicyResponse> couponPolicyList = couponPolicyService.getCouponPolicyList();
//
//        model.addAttribute("normalCoupons", normalCoupons);
//        model.addAttribute("bookCoupons", bookCoupons);
//        model.addAttribute("categoryCoupons", categoryCoupons);
//        model.addAttribute("couponPolicyList", couponPolicyList);
//
//        model.addAttribute("discountType", DiscountType.values());
//        model.addAttribute("status", CouponStatus.values());
//        model.addAttribute("couponType", CouponType.values());
//    }

    @GetMapping
    public String getCouponPage(Pageable pageable, Model model) {
        CouponPageResponse couponPageResponse = couponService.getCouponPageResponse(pageable);
        model.addAttribute("couponPageResponse", couponPageResponse);
        return "coupon/admin/coupon";
    }

    @PostMapping
    public String addCoupon(@Valid @ModelAttribute CouponRequest couponRequest,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("couponRequest", couponRequest);
        }else{
            couponService.createCoupon(couponRequest);
        }
        return REDIRECT_PATH;
    }

    @PutMapping("/{couponId}")
    public String updateCoupon(@PathVariable Long couponId, @ModelAttribute CouponRequest couponRequest) {
        couponService.updateCoupon(couponId, couponRequest);
        return REDIRECT_PATH;
    }

    @DeleteMapping("/{couponId}")
    public String deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return REDIRECT_PATH;
    }
}








