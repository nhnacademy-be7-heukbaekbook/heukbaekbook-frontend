package com.nhnacademy.heukbaekfrontend.couponset.coupon.controller;

import com.nhnacademy.heukbaekfrontend.HomeController;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponStatus;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.CouponService;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @MockBean
    private CouponPolicyService couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CouponController(couponService,couponPolicyService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetCouponPage() throws Exception {
        // Mock CouponPolicyResponse 데이터 생성
        CouponPolicyResponse mockPolicy = new CouponPolicyResponse(
                1L, DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                new BigDecimal("50.00"), new BigDecimal("500.00")
        );

        // Mock CouponResponse 데이터 생성
        Page<CouponResponse> mockNormalCoupons = new PageImpl<>(List.of(
                new CouponResponse(
                        1L, mockPolicy, "ABLE", 30,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(30),
                        "Normal Coupon", "Description of Normal Coupon",
                        LocalDateTime.now()
                )
        ));

        // Mock BookCouponResponse 데이터 생성
        Page<BookCouponResponse> mockBookCoupons = new PageImpl<>(List.of(
                new BookCouponResponse(
                        2L, "Book Coupon", "Discount on specific book",
                        LocalDateTime.now(), CouponStatus.ABLE, 15,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(15),
                        2L, DiscountType.FIXED, new BigDecimal("5000"),
                        new BigDecimal("30000"), new BigDecimal("100000"),
                        1001L, "Java Programming Book"
                )
        ));

        // Mock CategoryCouponResponse 데이터 생성
        Page<CategoryCouponResponse> mockCategoryCoupons = new PageImpl<>(List.of(
                new CategoryCouponResponse(
                        3L, "Category Coupon", "Discount on specific category",
                        LocalDateTime.now(), CouponStatus.DISABLE, 10,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(10),
                        3L, DiscountType.PERCENTAGE, new BigDecimal("5.0"),
                        new BigDecimal("20000"), new BigDecimal("80000"),
                        101L, "Technology"
                )
        ));

        // Mock CouponPolicyResponse List 생성
        List<CouponPolicyResponse> mockCouponPolicies = List.of(mockPolicy);

        // Mock 서비스 동작 설정
        when(couponService.getAllNormalCoupons(PageRequest.of(0, 10))).thenReturn(mockNormalCoupons);
        when(couponService.getAllBookCoupons(PageRequest.of(0, 10))).thenReturn(mockBookCoupons);
        when(couponService.getAllCategoryCoupons(PageRequest.of(0, 10))).thenReturn(mockCategoryCoupons);
        when(couponPolicyService.getCouponPolicyList()).thenReturn(mockCouponPolicies);

        // 테스트 수행
        mockMvc.perform(get("/admin/coupons")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon/admin/coupon"))
                .andExpect(model().attributeExists("normalCoupons"))
                .andExpect(model().attributeExists("bookCoupons"))
                .andExpect(model().attributeExists("categoryCoupons"))
                .andExpect(model().attributeExists("discountType"))
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attributeExists("couponPolicyList"))
                .andExpect(model().attribute("normalCoupons", mockNormalCoupons))
                .andExpect(model().attribute("bookCoupons", mockBookCoupons))
                .andExpect(model().attribute("categoryCoupons", mockCategoryCoupons))
                .andExpect(model().attribute("couponPolicyList", mockCouponPolicies));
    }

    @Test
    void testAddCouponWithValidationErrors() throws Exception {
        // Validation 오류가 있는 CouponRequest 데이터 생성
        CouponRequest invalidCouponRequest = new CouponRequest(
                null, // policyId가 null
                0, // 유효하지 않은 availableDuration
                null, // couponTimeStart가 null
                null,
                "", // 빈 couponName
                "A".repeat(501), // couponDescription의 길이 초과
                null,
                null
        );

        // 테스트 수행
        mockMvc.perform(post("/admin/coupons")
                        .flashAttr("couponRequest", invalidCouponRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons"))
                .andExpect(flash().attributeExists("couponRequest"));
    }

    @Test
    void testAddCouponWithValidData() throws Exception {
        // 유효한 CouponRequest 데이터 생성
        CouponRequest validCouponRequest = new CouponRequest(
                1L,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Valid Coupon",
                "This is a valid coupon description",
                101L,
                null
        );

        // Mock CouponResponse 생성
        CouponResponse mockResponse = new CouponResponse(
                1L,
                new CouponPolicyResponse(
                        1L, DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                        new BigDecimal("50.00"), new BigDecimal("500.00")
                ),
                "ABLE",
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Valid Coupon",
                "This is a valid coupon description",
                LocalDateTime.now()
        );

        // Mock 서비스 동작 설정
        when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(Optional.of(mockResponse));

        // 테스트 수행
        mockMvc.perform(post("/admin/coupons")
                        .flashAttr("couponRequest", validCouponRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons"));
    }

    @Test
    void testUpdateCoupon() throws Exception {
        // 유효한 CouponRequest 데이터 생성
        CouponRequest validCouponRequest = new CouponRequest(
                1L,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Updated Coupon",
                "This is an updated coupon description",
                101L,
                null
        );

        // Mock CouponResponse 생성
        CouponResponse mockResponse = new CouponResponse(
                1L,
                new CouponPolicyResponse(
                        1L, DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                        new BigDecimal("50.00"), new BigDecimal("500.00")
                ),
                "ABLE",
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Valid Coupon",
                "This is a valid coupon description",
                LocalDateTime.now()
        );

        // Mock 서비스 동작 설정
        when(couponService.updateCoupon(any(Long.class), any(CouponRequest.class))).thenReturn(Optional.of(mockResponse));

        // 테스트 수행
        mockMvc.perform(put("/admin/coupons/1")
                        .flashAttr("couponRequest", validCouponRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons"));
    }

    @Test
    void testDeleteCoupon() throws Exception {
        // Mock 서비스 동작 설정
        doNothing().when(couponService).deleteCoupon(any(Long.class));

        // 테스트 수행
        mockMvc.perform(delete("/admin/coupons/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons"));
    }

}
