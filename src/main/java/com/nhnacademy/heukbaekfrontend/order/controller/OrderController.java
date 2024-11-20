package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.book.domain.Book;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookOrderResponse;
import com.nhnacademy.heukbaekfrontend.book.dto.response.BookSummaryResponse;
import com.nhnacademy.heukbaekfrontend.book.service.BookService;
import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.service.CommonService;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final CartService cartService;

    private final CommonService commonService;

    @GetMapping
    public ModelAndView getOrderForm(@RequestParam(required = false) Long bookId,
                                     @RequestParam(required = false) Integer quantity,
                                     HttpSession session) {
        String sessionId = session.getId();
        log.info("bookId = {}, quantity = {}", bookId, quantity);

        List<Book> books = fetchBooks(sessionId, bookId, quantity);
        String totalPrice = commonService.calculateAllTotalPriceAndFormat(books);
        String totalDiscountAmount = commonService.calculateAllTotalDiscountAndFormat(books);

        return new ModelAndView("order/orderForm")
                .addObject("books", books)
                .addObject("totalPrice", totalPrice)
                .addObject("totalDiscountAmount", totalDiscountAmount);
    }

    private List<Book> fetchBooks(String sessionId, Long bookId, Integer quantity) {
        if (bookId == null && quantity == null) {
            return cartService.getBooksFromCart(sessionId); // 장바구니에서 책 가져오기
        }
        return orderService.getBookOrderResponses(bookId, quantity); // 주문 요청에서 책 가져오기
    }
}
