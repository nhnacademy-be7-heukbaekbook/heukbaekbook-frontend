package com.nhnacademy.heukbaekfrontend.common.aspect;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.dto.TokenRequest;
import com.nhnacademy.heukbaekfrontend.common.dto.TokenResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@Aspect
@Component
public class RoleAspect {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_MEMBER = "ROLE_MEMBER";

    private final CookieUtil cookieUtil;
    private final AuthClient authClient;

    @Autowired
    public RoleAspect(CookieUtil cookieUtil, AuthClient authClient) {
        this.cookieUtil = cookieUtil;
        this.authClient = authClient;
    }

    @Before("@annotation(com.nhnacademy.heukbaekfrontend.common.annotation.Admin)")
    public void checkAdminRole() {
        validateRoleWithServer(ROLE_ADMIN);
    }

    @Before("@annotation(com.nhnacademy.heukbaekfrontend.common.annotation.Member)")
    public void checkMemberRole() {
        validateRoleWithServer(ROLE_MEMBER);
    }

    private void validateRoleWithServer(String requiredRole) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new SecurityException("요청 속성을 사용할 수 없습니다.");
        }
        HttpServletRequest request = attributes.getRequest();

        String token = getAccessToken(request);
        if (token == null) {
            throw new SecurityException("토큰이 누락되었습니다.");
        }

        TokenRequest tokenRequest = new TokenRequest(token);

        try {
            ResponseEntity<TokenResponse> response;
            if (ROLE_ADMIN.equals(requiredRole)) {
                response = authClient.validateAdmin(tokenRequest);
            } else {
                response = authClient.validateMember(tokenRequest);
            }

            if (response.getStatusCode().isError()) {
                throw new SecurityException("사용자가 " + requiredRole + " 권한이 없습니다.");
            }

            request.setAttribute("id", Objects.requireNonNull(response.getBody()).id());
            request.setAttribute("role", Objects.requireNonNull(response.getBody().role()));
        } catch (FeignException e) {
            if (e.status() == 401) {
                throw new SecurityException("사용자가 " + requiredRole + " 권한이 없습니다.");
            }
            throw new SecurityException("권한 검증 중 예외가 발생했습니다.", e);
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String token = cookieUtil.getCookie(request, ACCESS_TOKEN);
        if (token == null) {
            token = (String) request.getAttribute(ACCESS_TOKEN);
        }
        return token;
    }
}
