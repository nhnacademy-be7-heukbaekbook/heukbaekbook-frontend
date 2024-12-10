package com.nhnacademy.heukbaekfrontend.order.controller.admin;

import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderResponse;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ModelAndView getOrders(@PageableDefault Pageable pageable) {
        log.info("pageable: {}", pageable);
        OrderResponse orderResponse = orderService.getOrders(pageable);

        return new ModelAndView("order/admin/orders")
                .addObject("orderResponse", orderResponse);
    }

    @GetMapping("/{orderId}")
    public ModelAndView getOrder(@PathVariable String orderId) {
        log.info("orderId: {}", orderId);

        OrderDetailResponse orderDetailResponse = orderService.createOrderDetailResponse(orderId);

        return new ModelAndView("order/admin/orderDetail")
                .addObject("orderDetailResponse", orderDetailResponse);
    }
}
