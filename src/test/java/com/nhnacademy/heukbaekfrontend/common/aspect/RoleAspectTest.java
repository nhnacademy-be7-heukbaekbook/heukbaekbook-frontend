package com.nhnacademy.heukbaekfrontend.common.aspect;

import com.nhnacademy.heukbaekfrontend.common.annotation.Admin;
import com.nhnacademy.heukbaekfrontend.common.annotation.Member;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AopTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@EnableAspectJAutoProxy(proxyTargetClass = true)
class RoleAspectTest {

    @Autowired
    private TestService testService;

    @Autowired
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        when(jwtUtil.resolveToken(mockRequest)).thenReturn("validAdminToken");
        when(jwtUtil.validateToken("validAdminToken")).thenReturn(true);
        when(jwtUtil.getRoleFromToken("validAdminToken")).thenReturn("ROLE_ADMIN");
    }

    @Test
    void testServiceAopProxy() {
        TestService targetTestService = AopTestUtils.getTargetObject(testService);

        assertFalse(AopUtils.isAopProxy(targetTestService));
        assertTrue(AopUtils.isAopProxy(testService));
    }

    @Test
    void testCheckAdminRole_withValidAdminToken() {
        assertDoesNotThrow(() -> testService.adminMethod());
    }

    @Test
    void testCheckMemberRole_withValidMemberToken() {
        when(jwtUtil.resolveToken(mockRequest)).thenReturn("validMemberToken");
        when(jwtUtil.validateToken("validMemberToken")).thenReturn(true);
        when(jwtUtil.getRoleFromToken("validMemberToken")).thenReturn("ROLE_MEMBER");

        assertDoesNotThrow(() -> testService.memberMethod());
    }

    @Test
    void testCheckRole_withNullAttributes() {
        RequestContextHolder.resetRequestAttributes();
        SecurityException exception = assertThrows(SecurityException.class, () -> testService.adminMethod());
        assertEquals("요청 속성을 사용할 수 없습니다.", exception.getMessage());
    }

    @Test
    void testCheckRole_withNullToken() {
        when(jwtUtil.resolveToken(mockRequest)).thenReturn(null);
        SecurityException exception = assertThrows(SecurityException.class, () -> testService.adminMethod());
        assertEquals("유효하지 않거나 누락된 토큰입니다.", exception.getMessage());
    }

    @Test
    void testCheckRole_withInvalidToken() {
        when(jwtUtil.validateToken("invalidToken")).thenReturn(false);
        when(jwtUtil.resolveToken(mockRequest)).thenReturn("invalidToken");

        SecurityException exception = assertThrows(SecurityException.class, () -> testService.adminMethod());
        assertEquals("유효하지 않거나 누락된 토큰입니다.", exception.getMessage());
    }

    @Test
    void testCheckRole_withWrongRole() {
        when(jwtUtil.getRoleFromToken("validAdminToken")).thenReturn("ROLE_MEMBER");

        SecurityException exception = assertThrows(SecurityException.class, () -> testService.adminMethod());
        assertEquals("사용자가 ROLE_ADMIN 권한이 없습니다.", exception.getMessage());
    }

    @Configuration
    static class Config {

        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
        }

        @Bean
        public RoleAspect roleAspect(JwtUtil jwtUtil) {
            return new RoleAspect(jwtUtil);
        }

        @Bean
        public TestService testService() {
            return new TestService();
        }
    }

    static class TestService {

        @Admin
        public void adminMethod() {
            System.out.println("Admin method executed.");
        }

        @Member
        public void memberMethod() {
            System.out.println("Member method executed.");
        }
    }
}
