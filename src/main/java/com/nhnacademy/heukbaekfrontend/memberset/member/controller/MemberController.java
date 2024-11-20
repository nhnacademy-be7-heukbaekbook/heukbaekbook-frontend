package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Member;
import com.nhnacademy.heukbaekfrontend.common.exception.ServerErrorException;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.LogoutService;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Member
    @GetMapping
    public String getMyPageHome(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute(MEMBER_RESPONSE, memberResponse);
        return "mypage/mypage";
    }

    @Member
    @GetMapping("/info")
    public String getMyPageInfo(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute(MEMBER_RESPONSE, memberResponse);
        return "mypage/mypage-info";
    }

    @Member
    @PostMapping("/info")
    public String doUpdateMyPageInfo(@Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MEMBER_RESPONSE, memberUpdateRequest);
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/members/mypage/info";
        }
        Optional<MemberResponse> optionalMemberResponse = memberService.updateMember(memberUpdateRequest);


        if (optionalMemberResponse.isEmpty()) {
            // error
            redirectAttributes.addFlashAttribute("error", "처리하지 못했습니다. 다시 시도해주세요.");
        } else {
            model.addAttribute(MEMBER_RESPONSE, optionalMemberResponse.get());
        }

        model.addAttribute("message", "정상적으로 처리되었습니다.");
        return "redirect:/members/mypage/info";
    }

    @Member
    @GetMapping("/withdraw")
    public String getMyPageWithdraw(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute(MEMBER_RESPONSE, memberResponse);
        return "mypage/mypage-withdraw";

    }

    @Member
    @PostMapping("/withdraw")
    public String doMemberWithdraw(HttpServletResponse response) {
        if (memberService.deleteMember()) {
            logoutService.logout(response);
        } else {
            throw new ServerErrorException();
        }
        return "redirect:/";
    }

}
