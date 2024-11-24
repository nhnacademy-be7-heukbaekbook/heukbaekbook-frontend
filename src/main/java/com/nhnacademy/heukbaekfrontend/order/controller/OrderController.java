package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderFormResponse;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

        OrderFormResponse orderFormResponse = orderService.createOrderFormResponse(sessionId, bookIds, quantity);
        log.info("orderFormResponse = {}", orderFormResponse);

        return new ModelAndView("order/orderForm")
                .addObject("orderFormResponse", orderFormResponse);
    }

    @GetMapping("/{orderId}")
    public ModelAndView getOrderDetailForm(@PathVariable String orderId) {
        log.info("orderId = {}", orderId);

        OrderDetailResponse orderDetailResponse = orderService.createOrderDetailResponse(orderId);
        log.info("orderDetailResponse = {}", orderDetailResponse);

        return new ModelAndView("order/detail")
                .addObject("orderDetailResponse", orderDetailResponse)
                .addObject("orderId", orderId);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        log.info("orderCreateRequest = {}", orderCreateRequest);

        return orderService.createOrder(orderCreateRequest);
    }
}
