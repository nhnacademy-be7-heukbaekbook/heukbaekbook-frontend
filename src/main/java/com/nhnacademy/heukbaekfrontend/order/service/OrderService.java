package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderFormRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderFormRequest createOrderFormRequest(String sessionId, List<Long> bookIds, Integer quantity);

    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);
}
