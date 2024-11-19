package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;

import java.util.List;

public interface OrderService {
    List<Book> getBookOrderResponses(Long bookId, int quantity);
}
