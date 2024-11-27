//package com.nhnacademy.heukbaekfrontend.order.service.impl;
//
//import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
//import com.nhnacademy.heukbaekfrontend.book.domain.Book;
//import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
//import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
//import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
//import com.nhnacademy.heukbaekfrontend.order.client.DeliveryFeeClient;
//import com.nhnacademy.heukbaekfrontend.order.client.OrderClient;
//import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderBookRequest;
//import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderBookResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderFormResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//
//class OrderServiceImplTest {
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @Mock
//    private BookClient bookClient;
//
//    @Mock
//    private CommonService commonService;
//
//    @Mock
//    private OrderClient orderClient;
//
//    @Mock
//    private CartService cartService;
//
//    @Mock
//    private DeliveryFeeClient deliveryFeeClient;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateOrderFormResponse() {
//        // Given
//        String sessionId = "session123";
//        List<Long> bookIds = List.of(1L, 2L);
//        Integer quantity = null;
//
//        List<Book> books = List.of(
//                new Book(1L, "Book A", true, "10000", "9000", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000), "thumbnail1", 1, "9000", BigDecimal.valueOf(9000)),
//                new Book(2L, "Book B", true, "20000", "18000", BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000), "thumbnail2", 1, "18000", BigDecimal.valueOf(18000))
//        );
//
//        BigDecimal totalPrice = BigDecimal.valueOf(27000);
//        BigDecimal deliveryFee = BigDecimal.valueOf(3000);
//
//        when(cartService.getBooksByBookIdsFromCart(sessionId, bookIds)).thenReturn(books);
//        when(commonService.calculateAllTotalPrice(books)).thenReturn(totalPrice);
//        when(commonService.calculateAllTotalPriceAndFormat(books)).thenReturn("27,000");
//        when(commonService.calculateAllTotalDiscountAndFormat(books)).thenReturn("3,000");
//        when(deliveryFeeClient.getDeliveryFeeByMinimumOrderAmount(totalPrice)).thenReturn(deliveryFee);
//        when(commonService.formatPrice(deliveryFee)).thenReturn("3,000");
//        when(commonService.formatPrice(totalPrice.add(deliveryFee))).thenReturn("30,000");
//
//        // When
//        OrderFormResponse result = orderService.createOrderFormResponse(sessionId, bookIds, quantity);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result.books()).hasSize(2);
//        assertThat(result.totalBookPrice()).isEqualTo("27,000");
//        assertThat(result.totalBookDiscountAmount()).isEqualTo("3,000");
//        assertThat(result.deliveryFee()).isEqualTo("3,000");
//        assertThat(result.totalPrice()).isEqualTo("30,000");
//
//        verify(cartService, times(1)).getBooksByBookIdsFromCart(sessionId, bookIds);
//        verify(commonService, times(1)).calculateAllTotalPrice(books);
//        verify(commonService, times(1)).calculateAllTotalPriceAndFormat(books);
//        verify(commonService, times(1)).calculateAllTotalDiscountAndFormat(books);
//        verify(deliveryFeeClient, times(1)).getDeliveryFeeByMinimumOrderAmount(totalPrice);
//    }
//
//    @Test
//    void testFetchBooks_FromCart() {
//        // Given
//        String sessionId = "session123";
//        List<Long> bookIds = List.of(1L, 2L);
//
//        List<Book> mockBooks = List.of(
//                new Book(1L, "Book A", true, "10000", "9000", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000), "thumbnail1", 1, "9000", BigDecimal.valueOf(9000)),
//                new Book(2L, "Book B", true, "20000", "18000", BigDecimal.valueOf(0.1), BigDecimal.valueOf(2000), "thumbnail2", 1, "18000", BigDecimal.valueOf(18000))
//        );
//
//        when(cartService.getBooksByBookIdsFromCart(sessionId, bookIds)).thenReturn(mockBooks);
//
//        // When
//        List<Book> result = orderService.fetchBooks(sessionId, bookIds, null);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).title()).isEqualTo("Book A");
//
//        verify(cartService, times(1)).getBooksByBookIdsFromCart(sessionId, bookIds);
//    }
//
//    @Test
//    void testFetchBooks_FromOrderRequest() {
//        // Given
//        List<Long> bookIds = List.of(1L);
//        Integer quantity = 2;
//
//        List<BookSummaryResponse> bookSummaries = List.of(
//                new BookSummaryResponse(1L, "Book A", true, BigDecimal.valueOf(10000), BigDecimal.valueOf(9000), BigDecimal.valueOf(0.1), "thumbnail1")
//        );
//
//        List<Book> expectedBooks = List.of(
//                new Book(1L, "Book A", true, "10000", "9000", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000), "thumbnail1", quantity, "18000", BigDecimal.valueOf(18000))
//        );
//
//        when(bookClient.getBooksSummary(anyList())).thenReturn(bookSummaries);
//        when(commonService.formatPrice(any(BigDecimal.class))).thenReturn("9000");
//        when(commonService.calculateTotalPriceAndFormat(any(BigDecimal.class), eq(quantity))).thenReturn("18000");
//        when(commonService.calculateTotalPrice(any(BigDecimal.class), eq(quantity))).thenReturn(BigDecimal.valueOf(18000));
//
//        // When
//        List<Book> result = orderService.fetchBooks(null, bookIds, quantity);
//
//        // Then
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).title()).isEqualTo("Book A");
//        assertThat(result.get(0).quantity()).isEqualTo(2);
//
//        verify(bookClient, times(1)).getBooksSummary(anyList());
//        verify(commonService, times(1)).calculateTotalPriceAndFormat(any(BigDecimal.class), eq(quantity));
//        verify(commonService, times(1)).calculateTotalPrice(any(BigDecimal.class), eq(quantity));
//    }
//
//    @Test
//    void testCreateOrder() {
//        // Given
//        List<OrderBookRequest> bookRequests = List.of(
//                new OrderBookRequest(1L, 2, "18000", true, 101L),
//                new OrderBookRequest(2L, 1, "15000", false, null)
//        );
//
//        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
//                "33,000",
//                "customer@example.com",
//                "John Doe",
//                "010-1234-5678",
//                "TOSS123",
//                "Jane Doe",
//                "12345",
//                "123 Main Street",
//                "Apt 101",
//                "010-8765-4321",
//                "3,000",
//                bookRequests
//        );
//
//        ResponseEntity<Long> mockResponse = ResponseEntity.ok(123L);
//
//        when(orderClient.createOrder(any(OrderCreateRequest.class))).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<Long> response = orderService.createOrder(orderCreateRequest);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.getBody()).isEqualTo(123L);
//
//        verify(orderClient, times(1)).createOrder(orderCreateRequest);
//    }
//
//    @Test
//    void testCreateOrderDetailResponse() {
//        // Given
//        String tossOrderId = "TOSS123";
//        List<OrderBookResponse> bookResponses = List.of(
//                new OrderBookResponse(
//                        "http://example.com/book1.jpg",
//                        "Book A",
//                        "20000",
//                        2,
//                        "18000",
//                        BigDecimal.valueOf(0.1),
//                        "36000"
//                ),
//                new OrderBookResponse(
//                        "http://example.com/book2.jpg",
//                        "Book B",
//                        "15000",
//                        1,
//                        "15000",
//                        BigDecimal.valueOf(0.0),
//                        "15000"
//                )
//        );
//
//        OrderDetailResponse mockResponse = new OrderDetailResponse(
//                "John Doe",
//                "3,000",
//                "36,000",
//                "Credit Card",
//                "Jane Doe",
//                12345L,
//                "123 Main Street",
//                "Apt 101",
//                "33,000",
//                "2,000",
//                "36,000",
//                bookResponses
//        );
//
//        when(orderClient.getOrderDetailResponse(tossOrderId)).thenReturn(mockResponse);
//
//        // When
//        OrderDetailResponse response = orderService.createOrderDetailResponse(tossOrderId);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.customerName()).isEqualTo("John Doe");
//        assertThat(response.books()).hasSize(2);
//
//        // Verify book details
//        assertThat(response.books().get(0).title()).isEqualTo("Book A");
//        assertThat(response.books().get(0).quantity()).isEqualTo(2);
//        assertThat(response.books().get(0).salePrice()).isEqualTo("18000");
//        assertThat(response.books().get(0).discountRate()).isEqualTo(BigDecimal.valueOf(0.1));
//
//        assertThat(response.books().get(1).title()).isEqualTo("Book B");
//        assertThat(response.books().get(1).quantity()).isEqualTo(1);
//        assertThat(response.books().get(1).salePrice()).isEqualTo("15000");
//        assertThat(response.books().get(1).discountRate()).isEqualTo(BigDecimal.valueOf(0.0));
//
//        verify(orderClient, times(1)).getOrderDetailResponse(tossOrderId);
//    }
//}
