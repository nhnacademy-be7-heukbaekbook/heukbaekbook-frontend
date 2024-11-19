package com.nhnacademy.heukbaekfrontend.order.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookOrderResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final BookClient bookClient;

    private final CommonService commonService;

    @Override
    public List<Book> getBookOrderResponses(Long bookId, int quantity) {
        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(List.of(bookId));
        return booksSummary.stream()
                .map(bookSummaryResponse -> new Book(
                        bookSummaryResponse.id(),
                        bookSummaryResponse.title(),
                        commonService.formatPrice(bookSummaryResponse.price()),
                        commonService.formatPrice(bookSummaryResponse.salePrice()),
                        bookSummaryResponse.discountRate(),
                        bookSummaryResponse.price().subtract(bookSummaryResponse.salePrice()),
                        bookSummaryResponse.thumbnailUrl(),
                        quantity,
                        commonService.calculateTotalPriceAndFormat(bookSummaryResponse.salePrice(), quantity),
                        commonService.calculateTotalPrice(bookSummaryResponse.salePrice(), quantity)

                ))
                .collect(Collectors.toList());
    }
}
