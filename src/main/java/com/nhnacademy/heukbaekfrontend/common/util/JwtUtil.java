package com.nhnacademy.heukbaekfrontend.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String EXP = "exp";

    private final ObjectMapper objectMapper;

    public boolean isExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return true;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> claims = objectMapper.readValue(payloadJson, new TypeReference<>() {
            });

            Date expiration = new Date(((Number) claims.get(EXP)).longValue() * 1000L);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}

