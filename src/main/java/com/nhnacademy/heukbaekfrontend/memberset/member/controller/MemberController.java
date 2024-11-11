package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberAddressDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/mypage")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String getMyPageHome(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("memberResponse", memberResponse);
        return "mypage/mypage";
    }

    @GetMapping("/info")
    public String getMyPageInfo(Model model) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("memberResponse", memberResponse);
        return "mypage/mypage-info";
    }

    @PostMapping("/info")
    public String updateMyPageInfo(@Valid @ModelAttribute MemberUpdateRequest memberUpdateRequest, Model model) {
        MemberResponse memberResponse = memberService.updateMember(memberUpdateRequest).getBody();
        model.addAttribute("memberResponse", memberResponse);
        return "mypage/mapage-info";
    }

    @GetMapping("/address")
    public String getMyPageAddress(Model model){
        ResponseEntity<List<MemberAddressDto>> memberAddressList = memberService.get

    }
}
