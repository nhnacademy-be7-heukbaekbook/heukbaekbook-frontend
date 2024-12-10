package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageOrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.CustomerService;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers/mypage")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/order/detail")
    public ModelAndView getCustomerDetailPage(@RequestParam String orderId,
                                              @RequestParam String email) {
        log.info("orderId : {}, email : {}", orderId, email);

        OrderDetailResponse orderDetailResponse = customerService.getCustomerOrderDetail(orderId, email);
        GradeDto gradeDto = new GradeDto("", BigDecimal.ZERO, BigDecimal.ZERO);

        MyPageOrderDetailResponse myPageOrderDetailResponse = new MyPageOrderDetailResponse(gradeDto, orderDetailResponse);

        return new ModelAndView("mypage/orderDetail")
                .addObject("myPageOrderDetailResponse", myPageOrderDetailResponse)
                .addObject("gradeDto", gradeDto);
    }
}
