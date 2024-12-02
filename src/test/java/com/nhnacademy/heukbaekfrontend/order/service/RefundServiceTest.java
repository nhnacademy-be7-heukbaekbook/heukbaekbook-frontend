package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.RefundClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundDetailResponse;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class RefundServiceTest {

    @Mock
    private RefundClient refundClient;

    @InjectMocks
    private RefundService refundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRefunds() {
        // Mock Static Method
        try (var mockedUtils = mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCustomerId).thenReturn("customerId123");

            // Mock Data
            GradeDto gradeDto = new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000));
            RefundDetailResponse refundDetailResponse = new RefundDetailResponse(
                    1L, "Damaged", "2023-12-01T12:00:00", "2023-12-02T12:00:00", "APPROVED",
                    101L, List.of("Book Title"), List.of(1), List.of(BigDecimal.valueOf(15000))
            );
            MyPageRefundDetailResponse response = new MyPageRefundDetailResponse(gradeDto, List.of(refundDetailResponse));

            when(refundClient.getRefunds("customerId123")).thenReturn(ResponseEntity.ok(response));

            // Test
            MyPageRefundDetailResponse result = refundService.getAllRefunds();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.gradeDto().gradeName()).isEqualTo("Gold");
            assertThat(result.refunds()).hasSize(1);
            assertThat(result.refunds().get(0).reason()).isEqualTo("Damaged");
        }
    }

    @Test
    void testCreateRefund() {
        // Mock Data
        RefundBookRequest refundBookRequest = new RefundBookRequest(1L, 1, BigDecimal.valueOf(15000));
        RefundCreateRequest request = new RefundCreateRequest(
                List.of(refundBookRequest), "paymentKey123", "Reason for cancel", "CARD"
        );
        RefundCreateResponse response = new RefundCreateResponse("Refund Processed");

        when(refundClient.createRefund(any(RefundCreateRequest.class))).thenReturn(ResponseEntity.ok(response));

        // Test
        RefundCreateResponse result = refundService.createRefund(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.message()).isEqualTo("Refund Processed");
    }
}
