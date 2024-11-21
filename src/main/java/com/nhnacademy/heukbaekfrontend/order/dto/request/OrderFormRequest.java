package com.nhnacademy.heukbaekfrontend.order.dto.request;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;

import java.util.List;

public record OrderFormRequest(List<Book> books,
                               String totalPrice,
                               String totalDiscountAmount) {
}
