package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberDetailResponse;

import java.util.List;

public record OrderFormResponse(MemberDetailResponse memberDetailResponse,
                                List<Book> books,
                                String totalBookPrice,
                                String totalBookDiscountAmount,
                                String deliveryFee,
                                String totalPrice) {
}
