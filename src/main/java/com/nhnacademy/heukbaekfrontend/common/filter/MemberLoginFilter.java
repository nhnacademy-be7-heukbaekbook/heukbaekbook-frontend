package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;


public class MemberLoginFilter extends BaseLoginFilter {
    private final LoginClient loginClient;

    public MemberLoginFilter(AuthenticationManager authenticationManager,
                             LoginClient loginClient,
                             CookieUtil cookieUtil,
                             CartService cartService) {
        super(authenticationManager, cookieUtil, "/login", cartService);
        this.loginClient = loginClient;
    }

    @Override
    protected ResponseEntity<LoginResponse> performLogin(LoginRequest loginRequest) {
        return loginClient.login(loginRequest);
    }

    @Override
    protected String getSuccessRedirectUrl() {
        return "/";
    }

    @Override
    protected String getFailureRedirectUrl() {
        return "/login";
    }
}
