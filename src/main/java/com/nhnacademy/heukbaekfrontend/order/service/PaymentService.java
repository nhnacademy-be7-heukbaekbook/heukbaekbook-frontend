package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.TossClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentCancelResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final TossClient tossClient;

    public PaymentService(TossClient tossClient) {
        this.tossClient = tossClient;
    }

    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        return tossClient.approvePayment(request).getBody();
    }

    public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
        return tossClient.cancelPayment(paymentKey, request).getBody();
    }

    public PaymentDetailResponse getPayment(Long paymentId) {
        return tossClient.getPayment(paymentId).getBody();
    }

    public List<PaymentDetailResponse> getPayments(Long customerId) {
        return tossClient.getPayments(customerId).getBody();
    }

}
