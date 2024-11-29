package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundableOrderDetailListResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundableOrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import com.nhnacademy.heukbaekfrontend.order.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;
    private final OrderService orderService;

    @GetMapping
    public String getAllRefunds(Model model) {
        MyPageRefundDetailResponse myPageRefundDetailResponse = refundService.getAllRefunds();
        model.addAttribute("gradeDto", myPageRefundDetailResponse.gradeDto());
        model.addAttribute("refunds", myPageRefundDetailResponse.refunds());
        return "refund/viewAllRefunds";
    }

    @GetMapping("/refundable-orders")
    public String getRefundableOrders(Model model) {
        MyPageRefundableOrderDetailListResponse myPageRefundableOrderDetailResponse = orderService.getRefundableOrders();
        model.addAttribute("gradeDto", myPageRefundableOrderDetailResponse.gradeDto());
        model.addAttribute("orders", myPageRefundableOrderDetailResponse.orders());
        return "refund/refundableOrders";
    }

    @GetMapping("/refundable-orders/{order-id}")
    public String getRefundableOrderDetail(@PathVariable(name = "order-id") Long orderId, Model model) {
        MyPageRefundableOrderDetailResponse myPageRefundableOrderDetailResponse = orderService.getRefundableOrderDetail(orderId);
        model.addAttribute("gradeDto", myPageRefundableOrderDetailResponse.gradeDto());
        model.addAttribute("orderDetail", myPageRefundableOrderDetailResponse.order());

        RefundCreateRequest refundCreateRequest = new RefundCreateRequest(new ArrayList<>(), null, null, "TOSS");

        for (int i = 0; i < myPageRefundableOrderDetailResponse.order().books().size() ; i++) {
            refundCreateRequest.refundBooks().add(new RefundBookRequest(null, null, null));
        }

        model.addAttribute("refundCreateRequest", refundCreateRequest);
        return "refund/refundableOrderDetail";
    }

    @PostMapping
    public String createRefund(@ModelAttribute RefundCreateRequest request, Model model) {
        RefundCreateResponse response = refundService.createRefund(request);
        return null;
    }
}
