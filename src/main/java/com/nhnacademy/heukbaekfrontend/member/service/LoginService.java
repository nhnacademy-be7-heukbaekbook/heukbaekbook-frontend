package com.nhnacademy.heukbaekfrontend.member.service;

import com.nhnacademy.heukbaekfrontend.member.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
    boolean login(LoginRequest loginRequest, HttpServletResponse response);
    boolean adminLogin(LoginRequest loginRequest, HttpServletResponse response);
}
