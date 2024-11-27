//package com.nhnacademy.heukbaekfrontend.order.service;
//
//import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class PaymentServiceTest {
//
//    @InjectMocks
//    private PaymentService paymentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testApprovePayment_Success() {
//        // Given
//        PaymentApprovalRequest request = new PaymentApprovalRequest(
//                "payment-key-123",
//                "order-id-456",
//                10000L
//        );
//
//        PaymentApprovalResponse mockResponse = new PaymentApprovalResponse("Payment approved successfully");
//
//        when(tossClient.approvePayment(any(PaymentApprovalRequest.class)))
//                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
//
//        // When
//        PaymentApprovalResponse result = paymentService.approvePayment(request);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.message()).isEqualTo("Payment approved successfully");
//        verify(tossClient, times(1)).approvePayment(any(PaymentApprovalRequest.class));
//    }
//
//    @Test
//    void testApprovePayment_Failure() {
//        // Given
//        PaymentApprovalRequest request = new PaymentApprovalRequest(
//                "payment-key-123",
//                "order-id-456",
//                10000L
//        );
//
//        when(tossClient.approvePayment(any(PaymentApprovalRequest.class)))
//                .thenThrow(new RuntimeException("Payment approval failed"));
//
//        // When & Then
//        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
//                RuntimeException.class,
//                () -> paymentService.approvePayment(request)
//        );
//
//        assertThat(exception.getMessage()).isEqualTo("Payment approval failed");
//        verify(tossClient, times(1)).approvePayment(any(PaymentApprovalRequest.class));
//    }
//}
