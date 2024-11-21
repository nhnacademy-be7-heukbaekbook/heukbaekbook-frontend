package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookOrderResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.actuator.HasFeatures;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final CartService cartService;

    private final CommonService commonService;

    private final CookieUtil cookieUtil;

    private final MemberService memberService;

    @GetMapping
    public ModelAndView getOrderForm(@RequestParam List<Long> bookIds,
                                     @RequestParam(required = false) Integer quantity,
                                     HttpSession session,
                                     HttpServletRequest request) {
        String sessionId = session.getId();

        log.info("bookIds = {}, quantity = {}, sessionId : {}", bookIds, quantity, sessionId);

        List<Book> books = fetchBooks(sessionId, bookIds, quantity);
        String totalPrice = commonService.calculateAllTotalPriceAndFormat(books);
        String totalDiscountAmount = commonService.calculateAllTotalDiscountAndFormat(books);

        return new ModelAndView("order/orderForm")
                .addObject("books", books)
                .addObject("totalPrice", totalPrice)
                .addObject("totalDiscountAmount", totalDiscountAmount);
    }

    private List<Book> fetchBooks(String sessionId, List<Long> bookIds, Integer quantity) {
        if (quantity == null) {
            return cartService.getBooksByBookIdsFromCart(sessionId, bookIds); // 장바구니에서 체크 표시한 책 가져오기
        }
        return orderService.getBookOrderResponses(bookIds.getFirst(), quantity); // 주문 요청에서 책 가져오기
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("orderCreateRequest = {}", orderCreateRequest);

        return orderService.createOrder(orderCreateRequest);
    }
}
