package com.nhnacademy.heukbaekfrontend.common.util;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private static final String TEST_SECRET_KEY = "testSecretKey12345678901234567890";
    private static final String ROLE = "ROLE_MEMBER";
    private static final Long EXPIRED_MS = 60 * 1000L;

    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;


    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil(TEST_SECRET_KEY);
    }

    @Test
    void testValidateToken_withValidToken() {
        String token = createTestToken(false);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testValidateToken_withExpiredToken() {
        String expiredToken = createTestToken(true);

        assertFalse(jwtUtil.validateToken(expiredToken));
    }

    @Test
    void testValidateToken_withInvalidToken() {
        String invalidToken = "invalid.token";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void testIsExpired_withExpiredToken() {
        String expiredToken = createTestToken(true);

        assertTrue(jwtUtil.isExpired(expiredToken));
    }

    @Test
    void testIsExpired_withValidToken() {
        String validToken = createTestToken(false);

        assertFalse(jwtUtil.isExpired(validToken));
    }

    @Test
    void testIsExpired_withEmptyToken() {
        String emptyToken = "";

        assertFalse(jwtUtil.isExpired(emptyToken));
    }

    @Test
    void testIsExpired_withNullToken() {

        assertFalse(jwtUtil.isExpired(null));
    }

    @Test
    void testGetRoleFromToken() {
        String token = createTestToken(false);

        String extractedRole = jwtUtil.getRoleFromToken(token);

        assertEquals(ROLE, extractedRole);
    }

    @Test
    void testResolveToken_withCookie() {
        Cookie[] cookies = {new Cookie(ACCESS_TOKEN, "testToken")};
        when(request.getCookies()).thenReturn(cookies);

        String token = jwtUtil.resolveToken(request);

        assertEquals("testToken", token);
    }

    @Test
    void testResolveToken_withoutCookie() {
        when(request.getCookies()).thenReturn(null);

        String token = jwtUtil.resolveToken(request);

        assertNull(token);
    }

    @Test
    void testResolveToken_withMultipleCookie_withoutAccessToken() {
        Cookie[] cookies = {
                new Cookie("otherToken", "otherValue"),
                new Cookie("anotherToken", "anotherValue")
        };
        when(request.getCookies()).thenReturn(cookies);

        String token = jwtUtil.resolveToken(request);

        assertNull(token);
    }


    private String createTestToken(boolean expired) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (expired ? -EXPIRED_MS : EXPIRED_MS));

        return Jwts.builder()
                .claim("role", ROLE)
                .expiration(expiryDate)
                .signWith(new SecretKeySpec(TEST_SECRET_KEY.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();
    }
}
