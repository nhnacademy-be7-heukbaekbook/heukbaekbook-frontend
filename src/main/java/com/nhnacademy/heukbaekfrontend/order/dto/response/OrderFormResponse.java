package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;

import java.util.List;

public record OrderFormResponse(List<Book> books,
                                String totalBookPrice,
                                String totalBookDiscountAmount,
                                String deliveryFee,
                                String totalPrice) {
}
