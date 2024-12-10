package com.nhnacademy.heukbaekfrontend.memberset.address.controller;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members/mypage/addresses")
public class MemberAddressController {

    private final MemberAddressService memberAddressService;
    private final MemberService memberService;

    private static final String REDIRECT_MY_PAGE_ADDRESS="redirect:/members/mypage/addresses";

    @GetMapping
    public String getMyPageAddresses(Model model) {
        List<MemberAddressResponse> addressList = memberAddressService.getMemberAddressesList();
        GradeDto gradeDto = memberService.getMembersGrade().get();
        model.addAttribute("gradeDto", gradeDto);
        model.addAttribute("addressList", addressList);
        return "mypage/mypage-address";
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMemberAddresses(){
        return memberAddressService.countMemberAddresses();
    }

    @PostMapping
    public String addMemberAddress(@Valid @ModelAttribute MemberAddressRequest memberAddressRequest,
                                   RedirectAttributes redirectAttributes,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return REDIRECT_MY_PAGE_ADDRESS;
        }
        memberAddressService.addMemberAddress(memberAddressRequest);

        return REDIRECT_MY_PAGE_ADDRESS;
    }

    @PutMapping("/{addressId}")
    public String updateMemberAddress(@PathVariable Long addressId, @Valid @ModelAttribute MemberAddressRequest memberAddressRequest) {
        memberAddressService.updateMemberAddress(addressId, memberAddressRequest);
        return REDIRECT_MY_PAGE_ADDRESS;
    }

    @DeleteMapping("/{addressId}")
    public String deleteMemberAddress(@PathVariable Long addressId) {
        memberAddressService.deleteMemberAddress(addressId);
        return REDIRECT_MY_PAGE_ADDRESS;
    }
}
