package com.nhnacademy.heukbaekfrontend.memberset.member.service;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;

public interface CustomerService {
    OrderDetailResponse getCustomerOrderDetail(String orderId, String email);
}
