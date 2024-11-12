package com.nhnacademy.heukbaekfrontend.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        jwtUtil = new JwtUtil(objectMapper);
    }

    @Test
    void testIsExpired_withValidTokenNotExpired() throws Exception {
        String token = createTokenWithExp(System.currentTimeMillis() / 1000 + 60);

        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    void testIsExpired_withValidTokenExpired() throws Exception {
        String token = createTokenWithExp(System.currentTimeMillis() / 1000 - 60);

        assertTrue(jwtUtil.isExpired(token));
    }

    @Test
    void testIsExpired_withInvalidTokenFormat() {
        String token = "invalid.token";

        assertTrue(jwtUtil.isExpired(token));
    }

    @Test
    void testIsExpired_withMalformedPayload() {
        String header = Base64.getUrlEncoder().encodeToString("{}".getBytes(StandardCharsets.UTF_8));
        String payload = "malformedPayload";
        String signature = "signature";
        String token = String.join(".", header, payload, signature);

        assertTrue(jwtUtil.isExpired(token));
    }

    @Test
    void testIsExpired_withoutExpClaim() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims);

        assertTrue(jwtUtil.isExpired(token));
    }

    @Test
    void testIsExpired_withNonNumericExpClaim() throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", "nonNumericValue");
        String token = createToken(claims);

        assertTrue(jwtUtil.isExpired(token));
    }

    private String createTokenWithExp(long exp) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", exp);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) throws Exception {
        String headerJson = objectMapper.writeValueAsString(new HashMap<String, Object>());
        String payloadJson = objectMapper.writeValueAsString(claims);

        String header = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signature = "signature";

        return String.join(".", header, payload, signature);
    }
}
