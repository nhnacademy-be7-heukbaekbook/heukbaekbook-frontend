package com.nhnacademy.heukbaekfrontend.couponset.coupon.service.impl;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.client.CouponClient;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.request.CouponRequest;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.BookCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CategoryCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.service.impl.CouponServiceImpl;
import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.CouponStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponClient couponClient;

    @Test
    void testGetAllNormalCoupons() {
        // Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        CouponPolicyResponse couponPolicy = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(50000)
        );

        CouponResponse coupon1 = new CouponResponse(
                1L,
                couponPolicy,
                "ACTIVE",
                30,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(25),
                "Coupon 1",
                "Description for Coupon 1",
                LocalDateTime.now().minusDays(5)
        );
        CouponResponse coupon2 = new CouponResponse(
                2L,
                couponPolicy,
                "INACTIVE",
                30,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1),
                "Coupon 2",
                "Description for Coupon 2",
                LocalDateTime.now().minusDays(10)
        );

        Page<CouponResponse> mockPage = new PageImpl<>(List.of(coupon1, coupon2));
        ResponseEntity<Page<CouponResponse>> mockResponse = ResponseEntity.ok(mockPage);

        when(couponClient.findAllNormalCoupons(anyInt(), anyInt(), anyString())).thenReturn(mockResponse);

        // When
        Page<CouponResponse> result = couponService.getAllNormalCoupons(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        CouponResponse firstCoupon = result.getContent().get(0);
        CouponPolicyResponse firstPolicy = firstCoupon.couponPolicyResponse();

        // Verify first coupon details
        assertThat(firstCoupon.couponName()).isEqualTo("Coupon 1");
        assertThat(firstCoupon.couponStatus()).isEqualTo("ACTIVE");
        assertThat(firstPolicy.discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(firstPolicy.discountAmount()).isEqualTo(BigDecimal.valueOf(10));

        CouponResponse secondCoupon = result.getContent().get(1);
        CouponPolicyResponse secondPolicy = secondCoupon.couponPolicyResponse();

        // Verify second coupon details
        assertThat(secondCoupon.couponName()).isEqualTo("Coupon 2");
        assertThat(secondCoupon.couponStatus()).isEqualTo("INACTIVE");
        assertThat(secondPolicy.minimumPurchaseAmount()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(secondPolicy.maximumPurchaseAmount()).isEqualTo(BigDecimal.valueOf(50000));

        verify(couponClient, times(1)).findAllNormalCoupons(0, 5, "couponCreatedAt, desc");
    }

    @Test
    void testGetAllBookCoupons() {
        // Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        BookCouponResponse coupon1 = new BookCouponResponse(
                1L,
                "Book Coupon 1",
                "Description for Book Coupon 1",
                LocalDateTime.now().minusDays(10),
                CouponStatus.ABLE,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(20),
                101L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(50000),
                1001L,
                "Book Title 1"
        );

        BookCouponResponse coupon2 = new BookCouponResponse(
                2L,
                "Book Coupon 2",
                "Description for Book Coupon 2",
                LocalDateTime.now().minusDays(15),
                CouponStatus.ABLE,
                30,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1),
                102L,
                DiscountType.FIXED,
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(30000),
                1002L,
                "Book Title 2"
        );

        Page<BookCouponResponse> mockPage = new PageImpl<>(List.of(coupon1, coupon2));
        ResponseEntity<Page<BookCouponResponse>> mockResponse = ResponseEntity.ok(mockPage);

        when(couponClient.findAllBookCoupons(anyInt(), anyInt(), anyString())).thenReturn(mockResponse);

        // When
        Page<BookCouponResponse> result = couponService.getAllBookCoupons(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        BookCouponResponse firstCoupon = result.getContent().get(0);
        assertThat(firstCoupon.couponName()).isEqualTo("Book Coupon 1");
        assertThat(firstCoupon.couponStatus()).isEqualTo(CouponStatus.ABLE);
        assertThat(firstCoupon.discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(firstCoupon.bookId()).isEqualTo(1001L);
        assertThat(firstCoupon.title()).isEqualTo("Book Title 1");

        BookCouponResponse secondCoupon = result.getContent().get(1);
        assertThat(secondCoupon.couponName()).isEqualTo("Book Coupon 2");
        assertThat(secondCoupon.couponStatus()).isEqualTo(CouponStatus.ABLE);
        assertThat(secondCoupon.discountType()).isEqualTo(DiscountType.FIXED);
        assertThat(secondCoupon.bookId()).isEqualTo(1002L);
        assertThat(secondCoupon.title()).isEqualTo("Book Title 2");

        verify(couponClient, times(1)).findAllBookCoupons(0, 5, "couponCreatedAt, desc");
    }

    @Test
    void testGetAllCategoryCoupons() {
        // Given
        Pageable pageable = Pageable.ofSize(5).withPage(0);

        CategoryCouponResponse coupon1 = new CategoryCouponResponse(
                1L,
                "Category Coupon 1",
                "Description for Category Coupon 1",
                LocalDateTime.now().minusDays(10),
                CouponStatus.ABLE,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(20),
                201L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(7000),
                BigDecimal.valueOf(70000),
                301L,
                "Category Name 1"
        );

        CategoryCouponResponse coupon2 = new CategoryCouponResponse(
                2L,
                "Category Coupon 2",
                "Description for Category Coupon 2",
                LocalDateTime.now().minusDays(15),
                CouponStatus.DISABLE,
                15,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now(),
                202L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(100000),
                302L,
                "Category Name 2"
        );

        Page<CategoryCouponResponse> mockPage = new PageImpl<>(List.of(coupon1, coupon2));
        ResponseEntity<Page<CategoryCouponResponse>> mockResponse = ResponseEntity.ok(mockPage);

        when(couponClient.findAllCategoryCoupons(anyInt(), anyInt(), anyString())).thenReturn(mockResponse);

        // When
        Page<CategoryCouponResponse> result = couponService.getAllCategoryCoupons(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        CategoryCouponResponse firstCoupon = result.getContent().get(0);
        assertThat(firstCoupon.couponName()).isEqualTo("Category Coupon 1");
        assertThat(firstCoupon.couponStatus()).isEqualTo(CouponStatus.ABLE);
        assertThat(firstCoupon.discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(firstCoupon.categoryId()).isEqualTo(301L);
        assertThat(firstCoupon.name()).isEqualTo("Category Name 1");

        CategoryCouponResponse secondCoupon = result.getContent().get(1);
        assertThat(secondCoupon.couponName()).isEqualTo("Category Coupon 2");
        assertThat(secondCoupon.couponStatus()).isEqualTo(CouponStatus.DISABLE);
        assertThat(secondCoupon.discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(secondCoupon.categoryId()).isEqualTo(302L);
        assertThat(secondCoupon.name()).isEqualTo("Category Name 2");

        verify(couponClient, times(1)).findAllCategoryCoupons(0, 5, "couponCreatedAt, desc");
    }

    @Test
    void testCreateCoupon_WithPolicy_Success() {
        // Given
        CouponRequest couponRequest = new CouponRequest(
                1L,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Test Coupon",
                "This is a test coupon.",
                null,
                null
        );

        CouponPolicyResponse couponPolicyResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(500)
        );

        CouponResponse couponResponse = new CouponResponse(
                1L,
                couponPolicyResponse,
                CouponStatus.ABLE.name(),
                30,
                couponRequest.couponTimeStart(),
                couponRequest.couponTimeEnd(),
                couponRequest.couponName(),
                couponRequest.couponDescription(),
                LocalDateTime.now()
        );

        when(couponClient.createCoupon(couponRequest)).thenReturn(ResponseEntity.ok(couponResponse));

        // When
        Optional<CouponResponse> result = couponService.createCoupon(couponRequest);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().couponName()).isEqualTo(couponRequest.couponName());
        assertThat(result.get().couponPolicyResponse().discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(result.get().couponPolicyResponse().discountAmount()).isEqualTo(BigDecimal.valueOf(10));
        assertThat(result.get().couponPolicyResponse().minimumPurchaseAmount()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(result.get().couponPolicyResponse().maximumPurchaseAmount()).isEqualTo(BigDecimal.valueOf(500));

        verify(couponClient, times(1)).createCoupon(couponRequest);
    }

    @Test
    void testCreateCoupon_EmptyResponse() {
        // Given
        CouponRequest couponRequest = new CouponRequest(
                2L,
                15,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(10),
                "Another Test Coupon",
                "This is another test coupon.",
                null,
                null
        );

        when(couponClient.createCoupon(couponRequest)).thenReturn(ResponseEntity.of(Optional.empty()));

        // When
        Optional<CouponResponse> result = couponService.createCoupon(couponRequest);

        // Then
        assertThat(result).isEmpty();

        verify(couponClient, times(1)).createCoupon(couponRequest);
    }

    @Test
    void testUpdateCoupon_Success() {
        // Given
        Long couponId = 1L;
        CouponRequest couponRequest = new CouponRequest(
                1L,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                "Updated Coupon",
                "Updated description",
                null,
                null
        );

        CouponPolicyResponse couponPolicyResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(1000)
        );

        CouponResponse couponResponse = new CouponResponse(
                couponId,
                couponPolicyResponse,
                CouponStatus.ABLE.name(),
                30,
                couponRequest.couponTimeStart(),
                couponRequest.couponTimeEnd(),
                couponRequest.couponName(),
                couponRequest.couponDescription(),
                LocalDateTime.now()
        );

        when(couponClient.updateCoupon(couponId, couponRequest)).thenReturn(ResponseEntity.ok(couponResponse));

        // When
        Optional<CouponResponse> result = couponService.updateCoupon(couponId, couponRequest);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().couponName()).isEqualTo(couponRequest.couponName());
        assertThat(result.get().couponPolicyResponse().discountAmount()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(result.get().couponPolicyResponse().maximumPurchaseAmount()).isEqualTo(BigDecimal.valueOf(1000));

        verify(couponClient, times(1)).updateCoupon(couponId, couponRequest);
    }

    @Test
    void testDeleteCoupon_Success() {
        // Given
        Long couponId = 1L;

        when(couponClient.deleteCoupon(couponId)).thenReturn(ResponseEntity.noContent().build());

        // When
        couponService.deleteCoupon(couponId);

        // Then
        verify(couponClient, times(1)).deleteCoupon(couponId);
    }


}
