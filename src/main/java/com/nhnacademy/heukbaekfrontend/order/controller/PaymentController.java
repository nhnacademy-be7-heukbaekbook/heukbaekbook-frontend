package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        log.info("view success");
        return "order/success";
    }

    @GetMapping("/fail")
    public String viewFail() {
        return "order/fail";
    }

    @PostMapping("/confirm")
    @ResponseBody
    public ResponseEntity<PaymentApprovalResponse> confirmPayment(@RequestBody PaymentApprovalRequest request) {
        log.info("confirm payment {}", request);
        PaymentApprovalResponse response = paymentService.approvePayment(request);
        log.info("결제 승인 요청 결과: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/result")
    public String viewResult(Model model) {
        model.addAttribute("response", new PaymentApprovalResponse("결제에 성공하였습니다."));
        return "order/detail";
    }
}
