package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.exception.ServerErrorException;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageOrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.CustomerService;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.LogoutService;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/mypage")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final LogoutService logoutService;

    public static final String MEMBER_RESPONSE = "memberResponse";

    @GetMapping
    public String getMyPageHome(Model model,
                                @PageableDefault Pageable pageable) {
        log.info("pageable: {}", pageable);
        MyPageResponse myPageResponse = memberService.createMyPageResponse(pageable);
//        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("gradeDto", myPageResponse.gradeDto());
        model.addAttribute("myPageResponse", myPageResponse);
        return "mypage/mypage";
    }

    @GetMapping("/info")
    public String getMyPageInfo(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute(MEMBER_RESPONSE, memberResponse);
        model.addAttribute("gradeDto", memberResponse.grade());
        return "mypage/mypage-info";
    }

    @PostMapping("/info")
    public String doUpdateMyPageInfo(@Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MEMBER_RESPONSE, memberUpdateRequest);
            redirectAttributes.addFlashAttribute("gradeDto", memberService.getMembersGrade().get());
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/members/mypage/info";
        }
        Optional<MemberResponse> optionalMemberResponse = memberService.updateMember(memberUpdateRequest);


        if (optionalMemberResponse.isEmpty()) {
            // error
            redirectAttributes.addFlashAttribute("error", "처리하지 못했습니다. 다시 시도해주세요.");
        } else {
            model.addAttribute("gradeDto", optionalMemberResponse.get().grade());
        }

        model.addAttribute("message", "정상적으로 처리되었습니다.");
        return "redirect:/members/mypage/info";
    }

    @GetMapping("/withdraw")
    public String getMyPageWithdraw(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("gradeDto", memberResponse.grade());
        return "mypage/mypage-withdraw";

    }

    @PostMapping("/withdraw")
    public String doMemberWithdraw(HttpServletResponse response) {
        if (memberService.deleteMember()) {
            logoutService.logout(response);
        } else {
            throw new ServerErrorException();
        }
        return "redirect:/";
    }

    @GetMapping("/orders")
    public ModelAndView getMyPageOrders(@PageableDefault Pageable pageable) {
        log.info("pageable: {}", pageable);
        MyPageResponse myPageResponse = memberService.createMyPageResponse(pageable);
        log.info("myPageResponse: {}", myPageResponse);

        return new ModelAndView("mypage/orders")
                .addObject("myPageResponse", myPageResponse)
                .addObject("gradeDto", myPageResponse.gradeDto());
    }

    @GetMapping("/order/detail")
    public ModelAndView getMyPageOrderDetail(@RequestParam String orderId) {
        log.info("orderId: {}", orderId);
        MyPageOrderDetailResponse myPageOrderDetailResponse = memberService.getMyPageOrderDetail(orderId);

        return new ModelAndView("mypage/orderDetail")
                .addObject("myPageOrderDetailResponse", myPageOrderDetailResponse)
                .addObject("gradeDto", myPageOrderDetailResponse.gradeDto());
    }

    @GetMapping("/reviews")
    public ModelAndView getMyPageReviews() {
        return new ModelAndView("mypage/reviews")
                .addObject("gradeDto", memberService.getMembersGrade().get());
    }
}
