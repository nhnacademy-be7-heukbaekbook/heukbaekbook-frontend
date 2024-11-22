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
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderFormRequest;
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

    @GetMapping
    public ModelAndView getOrderForm(@RequestParam List<Long> bookIds,
                                     @RequestParam(required = false) Integer quantity,
                                     HttpSession session) {
        String sessionId = session.getId();

        log.info("bookIds = {}, quantity = {}, sessionId : {}", bookIds, quantity, sessionId);

        OrderFormRequest orderFormRequest = orderService.createOrderFormRequest(sessionId, bookIds, quantity);
        log.info("orderFormRequest = {}", orderFormRequest);

        return new ModelAndView("order/orderForm")
                .addObject("orderFormRequest", orderFormRequest);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("orderCreateRequest = {}", orderCreateRequest);

        return orderService.createOrder(orderCreateRequest);
    }
}
