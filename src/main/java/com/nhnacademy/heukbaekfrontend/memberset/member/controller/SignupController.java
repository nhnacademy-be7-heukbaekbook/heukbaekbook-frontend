package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final MemberService memberService;

    @GetMapping
    public String getSignUpForm(Model model) {
        if(!model.containsAttribute("signupForm")){
            model.addAttribute("signupForm", new MemberCreateRequest(
                    null, null, null, null, null, null, null, null, null, null));
        }
        return "signup";
    }



    @PostMapping
    public String doSignup(@Valid @ModelAttribute MemberCreateRequest memberCreateRequest,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("signupForm", memberCreateRequest);
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/signup";
        }
        Optional<MemberResponse> optionalMemberResponse = memberService.signup(memberCreateRequest);

        if(optionalMemberResponse.isEmpty()){
            // error 페이지로 이동
        }

        model.addAttribute("name",optionalMemberResponse.get().name());

        return "signupSuccess";
    }

    @GetMapping("/check-duplicate/loginId/{loginId}")
    public ResponseEntity<Boolean> checkLoginIdDuplicate(@PathVariable String loginId){
        return memberService.existsLoginId(loginId);
    }

    @GetMapping("/check-duplicate/email/{email}")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return memberService.existsEmail(email);
    }
}
