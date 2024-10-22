package com.nhnacademy.heukbaekfrontend;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        model.addAttribute("ip", ip);
        return "index";
    }
}
