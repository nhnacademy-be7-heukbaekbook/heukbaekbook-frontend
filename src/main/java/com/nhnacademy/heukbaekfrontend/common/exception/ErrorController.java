package com.nhnacademy.heukbaekfrontend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/403")
    public String handleAccessDenied(Model model) {
        model.addAttribute("statusCode", HttpStatus.FORBIDDEN.value());
        model.addAttribute("statusName", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addAttribute("errorMessage", "이 페이지에 접근할 권한이 없습니다.");

        return "error/error-page";
    }
}
