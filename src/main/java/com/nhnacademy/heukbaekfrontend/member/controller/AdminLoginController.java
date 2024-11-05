package com.nhnacademy.heukbaekfrontend.member.controller;

import com.nhnacademy.heukbaekfrontend.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {
    private final LoginService loginService;

    @GetMapping("/admins/login")
    public String getAdminLoginForm() {
        return "/login/adminLogin";
    }

    @PostMapping("/admins/login")
    public String doAdminLogin(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, Model model) {
        boolean loginSuccess = loginService.adminLogin(loginRequest, response);

        if (!loginSuccess) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
            return "/login/adminLogin";
        }

        return "redirect:/admins/home";
    }
}
