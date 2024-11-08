package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String getSignUpForm(Model model) {
        if(!model.containsAttribute("signupForm")){
            model.addAttribute("signupForm", new MemberCreateRequest(
                    null, null, null, null, null, null, null, null, null, null));
        }
        return "signup";
    }

    @GetMapping("/signupSuccess")
    public String getSignUpForms(Model model) {

        return "signupSuccess";
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
        }else{
            model.addAttribute("name",optionalMemberResponse.get().name());
        }
        return "signupSuccess";
    }

    @PostMapping("/check-duplicate/loginId")
    public ResponseEntity<Boolean> checkLoginIdDuplicate(@RequestBody String loginId) {
        log.info("loginId: {}", loginId);
        return memberService.existsLoginId(loginId);
    }

    @PostMapping("/check-duplicate/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody String email){
        return memberService.existsEmail(email);
    }
}
