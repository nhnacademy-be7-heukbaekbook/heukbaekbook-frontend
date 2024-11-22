package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.PaymentClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentClient paymentClient;

    public PaymentService(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        return paymentClient.approvePayment(request).getBody();
    }
}
