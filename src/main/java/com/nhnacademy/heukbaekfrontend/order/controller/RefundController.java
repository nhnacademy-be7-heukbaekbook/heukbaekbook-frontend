package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import com.nhnacademy.heukbaekfrontend.order.service.RefundService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/refund")
public class RefundController {

    private final RefundService refundService;
    private final MemberService memberService;
    private final OrderService orderService;

    @GetMapping
    public String getAllRefunds(Model model) {
        MyPageRefundDetailResponse myPageRefundDetailResponse = refundService.getAllRefunds();
        model.addAttribute("myPageResponse", myPageRefundDetailResponse);
        return "refund/viewAllRefunds";
    }

    @GetMapping("/list")
    public String viewRequestRefund(Model model) {
        MyPageRefundableOrderDetailResponse myPageRefundableOrderDetailResponse = orderService.getRefundableOrders();
        model.addAttribute("myPageResponse", myPageRefundableOrderDetailResponse);
        return "refund/selectRefundableOrders";
    }

    @GetMapping("/request/{order-id}")
    public String viewRefundApplication(@PathVariable(name = "order-id") Long orderId, Model model, HttpSession session) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        List<RefundableOrderDetailResponse> orders = (List<RefundableOrderDetailResponse>) session.getAttribute("refundableOrders");

        if (orders == null || orders.isEmpty()) {
            return "redirect:/refund/list";
        }

        RefundableOrderDetailResponse orderDetail = orders.stream()
                .filter(order -> order.orderId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (orderDetail == null) {
            model.addAttribute("errorMessage", "해당 주문을 찾을 수 없습니다.");
            return "redirect:/refund/list";
        }

        List<RefundBookRequest> refundBooks = new ArrayList<>();
        for (int i = 0; i < orderDetail.books().size(); i++) {
            RefundBookRequest bookRequest = new RefundBookRequest(
                    orderDetail.books().get(i).bookId(),
                    0,
                    new BigDecimal(orderDetail.books().get(i).price())
            );
            refundBooks.add(bookRequest);
        }

        RefundCreateRequest refundCreateRequest = new RefundCreateRequest(
                refundBooks,
                orderDetail.paymentKey(),
                "",
                "TOSS"
        );

        model.addAttribute("memberResponse", memberResponse);
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("refundCreateRequest", refundCreateRequest);
        return "refund/refundOrderDetail";
    }

    @PostMapping("/request")
    public String requestRefund(@ModelAttribute RefundCreateRequest request, Model model) {

        if (request.cancelReason() == null || request.cancelReason().isEmpty()) {
            model.addAttribute("errorMessage", "환불 사유를 선택하거나 입력해주세요.");
            return "redirect:/refund/request";
        }

        List<RefundBookRequest> selectedBooks = request.refundBooks().stream()
                .filter(book -> book.bookId() != null && book.quantity() != null && book.quantity() > 0)
                .collect(Collectors.toList());

        if (selectedBooks.isEmpty()) {
            model.addAttribute("errorMessage", "환불할 도서를 선택해주세요.");
            return "redirect:/refund/request";
        }

        RefundCreateRequest newRequest = new RefundCreateRequest(
                selectedBooks,
                request.paymentKey(),
                request.cancelReason(),
                request.method()
        );

        RefundCreateResponse response = refundService.requestRefund(newRequest);

        model.addAttribute("response", response);

        return "redirect:/refund";
    }
}
