package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentCancelResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public String viewOrderSheet() {
        return "order/orderSheet";
    }

    @GetMapping("/success")
    public String viewSuccess() {
        return "order/success";
    }

    @GetMapping("/fail")
    public String viewFail() {
        return "order/fail";
    }

    @GetMapping("/result")
    public String viewResult() {
        return "order/result";
    }

    @PostMapping("/confirm")
    public String confirmPayment(@RequestBody PaymentApprovalRequest request, Model model) {
        PaymentApprovalResponse response = paymentService.approvePayment(request);
        model.addAttribute("response", response);
        log.info("결제 승인 요청 결과: {}", response.toString());
        return "order/result";
    }

    @PostMapping("/{payment-key}/cancel")
    public String cancelPayment(
            @PathVariable(name = "payment-key") String paymentKey,
            @RequestBody PaymentCancelRequest request,
            Model model) {
        PaymentCancelResponse response = paymentService.cancelPayment(paymentKey, request);
        model.addAttribute("response", response);
        log.info("결제 취소 요청 결과: {}", response.toString());
        return "order/result";
    }

    @GetMapping("/{payment-id}")
    public String getPayment(@PathVariable(name = "payment-id") Long paymentId, Model model) {
        PaymentDetailResponse payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "order/result";
    }

    @GetMapping("/{customer-id}")
    public String getPayments(@PathVariable(name = "customer-id") Long customerId, Model model) {
        List<PaymentDetailResponse> payments = paymentService.getPayments(customerId);
        model.addAttribute("payments", payments);
        return "order/result";
    }

}
