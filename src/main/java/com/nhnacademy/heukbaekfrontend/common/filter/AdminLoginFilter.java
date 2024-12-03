package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;

public class AdminLoginFilter extends BaseLoginFilter {
    private final LoginClient loginClient;

    public AdminLoginFilter(AuthenticationManager authenticationManager,
                            LoginClient loginClient,
                            CookieUtil cookieUtil,
                            CartService cartService) {
        super(authenticationManager, cookieUtil, "/admin/login", cartService);
        this.loginClient = loginClient;
    }

    @Override
    protected ResponseEntity<LoginResponse> performLogin(LoginRequest loginRequest) {
        return loginClient.adminLogin(loginRequest);
    }

    @Override
    protected String getSuccessRedirectUrl() {
        return "/admin/home";
    }
}
