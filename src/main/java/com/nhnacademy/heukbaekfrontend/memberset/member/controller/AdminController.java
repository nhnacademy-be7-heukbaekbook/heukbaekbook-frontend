package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @Admin
    @GetMapping("/admins/home")
    public String getAdminHome() {
        return "admin/adminHome";
    }
}
