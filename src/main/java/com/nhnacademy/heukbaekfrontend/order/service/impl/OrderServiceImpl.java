package com.nhnacademy.heukbaekfrontend.order.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.client.DeliveryFeeClient;
import com.nhnacademy.heukbaekfrontend.order.client.OrderClient;
import com.nhnacademy.heukbaekfrontend.order.client.WrappingPaperClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import com.nhnacademy.heukbaekfrontend.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private final DeliveryFeeClient deliveryFeeClient;

    private final MemberClient memberClient;

    private final WrappingPaperClient wrappingPaperClient;

    @Override
    public OrderFormResponse createOrderFormResponse(String sessionId, List<Long> bookIds, Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetailResponse memberDetailResponse = null;

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            memberDetailResponse = memberClient.getMemberDetail();
        }

        List<Book> books = fetchBooks(sessionId, bookIds, quantity);
        BigDecimal totalPrice = commonService.calculateAllTotalPrice(books);
        String totalBookPrice = commonService.calculateAllTotalPriceAndFormat(books);
        String totalBookDiscountAmount = commonService.calculateAllTotalDiscountAndFormat(books);

        BigDecimal deliveryFee = deliveryFeeClient.getDeliveryFeeByMinimumOrderAmount(totalPrice);
        totalPrice = totalPrice.add(deliveryFee);

        log.info("memberDetailResponse: {}", memberDetailResponse);

        List<WrappingPaperResponse> wrappingPapers = wrappingPaperClient.getAllWrappingPapers();

        return new OrderFormResponse(
                memberDetailResponse,
                books,
                totalBookPrice,
                totalBookDiscountAmount,
                commonService.formatPrice(deliveryFee),
                commonService.formatPrice(totalPrice),
                wrappingPapers
        );
    }

    List<Book> fetchBooks(String sessionId, List<Long> bookIds, Integer quantity) {
        if (quantity == null) {
            log.info("sessionId {} bookIds {}", sessionId, bookIds);
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
                        bookSummaryResponse.wrappingPaperId(),
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

    @Override
    public OrderDetailResponse createOrderDetailResponse(String tossOrderId) {
        return orderClient.getOrderDetailResponse(tossOrderId);
    }

    @Override
    public MyPageRefundableOrderDetailListResponse getRefundableOrders() {
        String customerId = Utils.getCustomerId();
        return orderClient.getRefundableOrders(customerId).getBody();
    }

    @Override
    public MyPageRefundableOrderDetailResponse getRefundableOrderDetail(Long orderId) {
        String customerId = Utils.getCustomerId();
        return orderClient.getRefundableOrderDetail(customerId, orderId).getBody();
    }

    @Override
    public void deleteOrder(String orderId) {
        orderClient.deleteOrder(orderId);
    }

    @Override
    public OrderResponse getOrders(Pageable pageable) {
        return orderClient.getOrders(pageable);
    }

    @Override
    public void updateOrder(String orderId, OrderUpdateRequest orderUpdateRequest) {
        orderClient.updateOrder(orderId, orderUpdateRequest);
    }
}
