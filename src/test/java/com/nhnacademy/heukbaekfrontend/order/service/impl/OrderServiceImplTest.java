package com.nhnacademy.heukbaekfrontend.order.service.impl;

import com.nhnacademy.heukbaekfrontend.book.client.BookClient;
import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.client.DeliveryFeeClient;
import com.nhnacademy.heukbaekfrontend.order.client.OrderClient;
import com.nhnacademy.heukbaekfrontend.order.client.WrappingPaperClient;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderServiceImpl orderService;

    @Mock
    private BookClient bookClient;

    @Mock
    private CommonService commonService;

    @Mock
    private OrderClient orderClient;

    @Mock
    private CartService cartService;

    @Mock
    private DeliveryFeeClient deliveryFeeClient;

    @Mock
    private MemberClient memberClient;

    @Mock
    private WrappingPaperClient wrappingPaperClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Manually create orderService and inject mocks
        orderService = new OrderServiceImpl(
                bookClient,
                commonService,
                orderClient,
                cartService,
                deliveryFeeClient,
                memberClient,
                wrappingPaperClient
        );
    }

    @Test
    void testFetchBooks_FromCart() {
        String sessionId = "sessionId";
        List<Long> bookIds = List.of(1L);

        List<Book> mockBooks = List.of(
                new Book(1L, "Book A", true, 1L, "10000", "9000", BigDecimal.valueOf(0.1),
                        BigDecimal.valueOf(1000), "url", 2, "18000", BigDecimal.valueOf(18000))
        );

        when(cartService.getBooksByBookIdsFromCart(sessionId, bookIds)).thenReturn(mockBooks);

        List<Book> result = orderService.fetchBooks(sessionId, bookIds, null);

        assertThat(result).isEqualTo(mockBooks);
        verify(cartService, times(1)).getBooksByBookIdsFromCart(sessionId, bookIds);
    }

    @Test
    void testFetchBooks_FromRequest() {
        List<Long> bookIds = List.of(1L);
        int quantity = 2;

        BookSummaryResponse bookSummary = new BookSummaryResponse(1L, "Book A", true, 1L,
                BigDecimal.valueOf(10000), BigDecimal.valueOf(9000), BigDecimal.valueOf(0.1), "url");
        when(bookClient.getBooksSummary(anyList())).thenReturn(List.of(bookSummary));
        when(commonService.calculateTotalPriceAndFormat(any(BigDecimal.class), eq(quantity))).thenReturn("18000");
        when(commonService.calculateTotalPrice(any(BigDecimal.class), eq(quantity))).thenReturn(BigDecimal.valueOf(18000));

        List<Book> result = orderService.fetchBooks(null, bookIds, quantity);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Book A");
        verify(bookClient, times(1)).getBooksSummary(bookIds);
    }

    @Test
    void testCreateOrder() {
        OrderCreateRequest request = new OrderCreateRequest(
                1L, "20000", "john.doe@example.com", "John Doe", "010-1234-5678",
                "TOSS123", "Jane Doe", "12345", "123 Main St", "Apt 101",
                "010-8765-4321", "3000", "1000", List.of()
        );

        when(orderClient.createOrder(any(OrderCreateRequest.class))).thenReturn(ResponseEntity.ok(1L));

        ResponseEntity<Long> result = orderService.createOrder(request);

        assertThat(result.getBody()).isEqualTo(1L);
        verify(orderClient, times(1)).createOrder(request);
    }

    @Test
    void testCreateOrderDetailResponse() {
        String tossOrderId = "TOSS123";
        OrderDetailResponse mockResponse = new OrderDetailResponse(
                "John Doe", "3000", "20000", "Credit Card", "Jane Doe", 12345L,
                "123 Main St", "Apt 101", "15000", "3000", "18000", "DELIVERED", List.of()
        );

        when(orderClient.getOrderDetailResponse(anyString())).thenReturn(mockResponse);

        OrderDetailResponse result = orderService.createOrderDetailResponse(tossOrderId);

        assertThat(result).isNotNull();
        assertThat(result.customerName()).isEqualTo("John Doe");
        verify(orderClient, times(1)).getOrderDetailResponse(tossOrderId);
    }

    @Test
    void testGetRefundableOrders() {
        // Mock response data
        MyPageRefundableOrderDetailListResponse mockResponse = new MyPageRefundableOrderDetailListResponse(
                new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000)),
                List.of()
        );

        // Use any() to match any value including null
        when(orderClient.getRefundableOrders(any()))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Call method
        MyPageRefundableOrderDetailListResponse result = orderService.getRefundableOrders();

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.gradeDto().gradeName()).isEqualTo("Gold");
        verify(orderClient, times(1)).getRefundableOrders(any());
    }
    @Test
    void testCreateOrderFormResponse() {
        String sessionId = "sessionId";
        List<Long> bookIds = List.of(1L);
        Integer quantity = 2;

        // 목 데이터 생성
        MemberDetailResponse mockMemberDetailResponse = new MemberDetailResponse(
                1L, "John Doe", "010-1234-5678", "john.doe@example.com",
                BigDecimal.valueOf(10000), List.of()
        );

        BookSummaryResponse bookSummary = new BookSummaryResponse(
                1L, "Book A", true, 1L,
                BigDecimal.valueOf(10000), BigDecimal.valueOf(9000),
                BigDecimal.valueOf(0.1), "url"
        );

        List<Book> mockBooks = List.of(
                new Book(1L, "Book A", true, 1L, "10000", "9000", BigDecimal.valueOf(0.1),
                        BigDecimal.valueOf(1000), "url", quantity, "18000", BigDecimal.valueOf(18000))
        );

        BigDecimal mockDeliveryFee = BigDecimal.valueOf(3000);
        List<WrappingPaperResponse> mockWrappingPapers = List.of(
                new WrappingPaperResponse(1L, "Gift Wrap", BigDecimal.valueOf(500), "url")
        );

        // 목 반환값 설정
        when(memberClient.getMemberDetail()).thenReturn(mockMemberDetailResponse);
        when(bookClient.getBooksSummary(anyList())).thenReturn(List.of(bookSummary));
        when(commonService.formatPrice(any(BigDecimal.class))).thenAnswer(invocation -> invocation.getArgument(0).toString());
        when(commonService.calculateTotalPriceAndFormat(any(BigDecimal.class), eq(quantity))).thenReturn("18000");
        when(commonService.calculateTotalPrice(any(BigDecimal.class), eq(quantity))).thenReturn(BigDecimal.valueOf(18000));
        when(commonService.calculateAllTotalPrice(anyList())).thenReturn(BigDecimal.valueOf(18000));
        when(commonService.calculateAllTotalPriceAndFormat(anyList())).thenReturn("18000");
        when(commonService.calculateAllTotalDiscountAndFormat(anyList())).thenReturn("2000");
        when(deliveryFeeClient.getDeliveryFeeByMinimumOrderAmount(any(BigDecimal.class))).thenReturn(mockDeliveryFee);
        when(wrappingPaperClient.getAllWrappingPapers()).thenReturn(mockWrappingPapers);

        // 인증 컨텍스트 설정
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // 메서드 호출
        OrderFormResponse result = orderService.createOrderFormResponse(sessionId, bookIds, quantity);

        // 검증
        assertThat(result).isNotNull();
        assertThat(result.memberDetailResponse()).isEqualTo(mockMemberDetailResponse);
        assertThat(result.books()).hasSize(1);
        assertThat(result.deliveryFee()).isEqualTo("3000");
        verify(memberClient, times(1)).getMemberDetail();
    }

    @Test
    void testGetRefundableOrderDetail() {
        // Mock data
        MyPageRefundableOrderDetailResponse mockResponse = new MyPageRefundableOrderDetailResponse(
                new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000)),
                new RefundableOrderDetailResponse(1L, "John Doe", "3000", "20000", "Credit Card", "Jane Doe", 12345L,
                        "123 Main St", "Apt 101", "15000", "3000", "18000", List.of(), null, "DELIVERED", "KEY123")
        );

        // Use any() to match any value including null
        when(orderClient.getRefundableOrderDetail(any(), any()))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Call method
        MyPageRefundableOrderDetailResponse result = orderService.getRefundableOrderDetail(1L);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.gradeDto().gradeName()).isEqualTo("Gold");
        verify(orderClient, times(1)).getRefundableOrderDetail(any(), eq(1L));
    }
}
