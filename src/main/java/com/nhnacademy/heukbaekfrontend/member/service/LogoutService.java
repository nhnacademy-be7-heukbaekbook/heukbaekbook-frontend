package com.nhnacademy.heukbaekfrontend.member.service;

import jakarta.servlet.http.HttpServletResponse;

public interface LogoutService {
    void logout(HttpServletResponse response);
}
