package com.nhnacademy.heukbaekfrontend.order.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookOrderResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.order.client.OrderClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderFormRequest;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final BookClient bookClient;

    private final CommonService commonService;

    private final OrderClient orderClient;

    private final CartService cartService;

    @Override
    public OrderFormRequest createOrderFormRequest(String sessionId, List<Long> bookIds, Integer quantity) {
        List<Book> books = fetchBooks(sessionId, bookIds, quantity);
        String totalPrice = commonService.calculateAllTotalPriceAndFormat(books);
        String totalDiscountAmount = commonService.calculateAllTotalDiscountAndFormat(books);

        return new OrderFormRequest(books, totalPrice, totalDiscountAmount);
    }

    private List<Book> fetchBooks(String sessionId, List<Long> bookIds, Integer quantity) {
        if (quantity == null) {
            log.info("sessiondId {} bookIds {}", sessionId, bookIds);
            return cartService.getBooksByBookIdsFromCart(sessionId, bookIds); // 장바구니에서 체크 표시한 책 가져오기
        }
        return getBookOrderResponses(bookIds.getFirst(), quantity); // 주문 요청에서 책 가져오기
    }

    private List<Book> getBookOrderResponses(Long bookId, int quantity) {
        List<BookSummaryResponse> booksSummary = bookClient.getBooksSummary(List.of(bookId));
        return booksSummary.stream()
                .map(bookSummaryResponse -> new Book(
                        bookSummaryResponse.id(),
                        bookSummaryResponse.title(),
                        bookSummaryResponse.isPackable(),
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

    @Override
    public ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest) {
        return orderClient.createOrder(orderCreateRequest);
    }
}
