package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.CustomerClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.CustomerService;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerClient customerClient;

    @Override
    public OrderDetailResponse getCustomerOrderDetail(String orderId, String email) {
        return customerClient.getOrderDetailResponse(orderId, email);
    }
}
