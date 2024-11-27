package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.client.RefundClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekfrontend.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;

    public MyPageRefundDetailResponse getAllRefunds() {
        String customerId = Utils.getCustomerId();
        return refundClient.getRefunds(customerId).getBody();
    }

    public RefundCreateResponse requestRefund(RefundCreateRequest request) {
        return refundClient.requestRefund(request).getBody();
    }
}
