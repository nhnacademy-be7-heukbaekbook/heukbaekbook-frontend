package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    List<Book> getBookOrderResponses(Long bookId, int quantity);

    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);
}
