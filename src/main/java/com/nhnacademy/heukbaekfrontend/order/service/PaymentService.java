package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.TossClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final TossClient tossClient;

    public PaymentService(TossClient tossClient) {
        this.tossClient = tossClient;
    }

    public PaymentApprovalResponse approvePayment(PaymentApprovalRequest request) {
        return tossClient.approvePayment(request).getBody();
    }
}
