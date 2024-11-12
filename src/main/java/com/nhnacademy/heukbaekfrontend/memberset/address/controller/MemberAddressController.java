package com.nhnacademy.heukbaekfrontend.memberset.address.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Member;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/mypage/addresses")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;
    private final MemberService memberService;

    @Member
    @GetMapping
    public String getMyPageAddresses(Model model) {
        List<MemberAddressDto> addressList = memberAddressService.getMemberAddressesList();
        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("memberResponse", memberResponse);
        model.addAttribute("addressList", addressList);
        return "mypage/mypage-address";
    }

    @Member
    @GetMapping("/count")
    public Long countMemberAddresses(){
        return memberAddressService.countMemberAddresses();
    }
}
