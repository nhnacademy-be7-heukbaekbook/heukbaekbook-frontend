package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String getLoginForm() {
        return "login/login";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, Model model) {
        boolean loginSuccess = loginService.login(loginRequest, response);

        if (!loginSuccess) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            return "login/login";
        }

        return "redirect:/";
    }

}
