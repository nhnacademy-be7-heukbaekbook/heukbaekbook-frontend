package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PostMapping("/confirm")
    public String confirmPayment(@RequestBody PaymentApprovalRequest request, Model model) {
        PaymentApprovalResponse response = paymentService.approvePayment(request);
        model.addAttribute("response", response);
        log.info("결제 승인 요청 결과: {}", response.toString());
        return "redirect:/toss/result";
    }

}
