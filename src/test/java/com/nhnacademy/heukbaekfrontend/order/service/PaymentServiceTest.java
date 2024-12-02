package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.PaymentClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApprovePayment() {
        // Mock Data
        PaymentApprovalRequest request = new PaymentApprovalRequest("paymentKey123", "orderId456", 50000L, "CARD");
        PaymentApprovalResponse response = new PaymentApprovalResponse("Payment Approved");

        when(paymentClient.approvePayment(any(PaymentApprovalRequest.class)))
                .thenReturn(ResponseEntity.ok(response));

        // Test
        PaymentApprovalResponse result = paymentService.approvePayment(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.message()).isEqualTo("Payment Approved");
    }
}
