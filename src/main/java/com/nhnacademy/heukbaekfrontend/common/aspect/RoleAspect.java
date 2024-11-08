package com.nhnacademy.heukbaekfrontend.common.aspect;

import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JwtUtil jwtUtil;

    @Autowired
    public RoleAspect(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Before("@annotation(com.nhnacademy.heukbaekfrontend.common.annotation.Admin)")
    public void checkAdminRole() {
        checkRole(ROLE_ADMIN);
    }

    @Before("@annotation(com.nhnacademy.heukbaekfrontend.common.annotation.Member)")
    public void checkMemberRole() {
        checkRole(ROLE_MEMBER);
    }

    private void checkRole(String requiredRole) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new SecurityException("요청 속성을 사용할 수 없습니다.");
        }
        HttpServletRequest request = attributes.getRequest();

        String token = getAccessToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            throw new SecurityException("유효하지 않거나 누락된 토큰입니다.");
        }

        String role = jwtUtil.getRoleFromToken(token);
        if (!requiredRole.equals(role)) {
            throw new SecurityException("사용자가 " + requiredRole + " 권한이 없습니다.");
        }
    }

    private String getAccessToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            token = (String) request.getAttribute(ACCESS_TOKEN);
        }

        return token;
    }
}