package com.nhnacademy.heukbaekfrontend.common.filter.wrapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;

import java.util.*;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders;
    private String customCookieHeader;
    private Cookie[] customCookies;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public void setCookies(Cookie[] cookies) {
        this.customCookies = cookies;
        this.customCookieHeader = generateCookieHeader(cookies);
    }

    private String generateCookieHeader(Cookie[] cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : cookies) {
            if (!cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }

    @Override
    public String getHeader(String name) {
        if (HttpHeaders.COOKIE.equalsIgnoreCase(name) && customCookieHeader != null) {
            return customCookieHeader;
        }
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());

        Enumeration<String> e = super.getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        if (customCookieHeader != null) {
            set.add(HttpHeaders.COOKIE);
        }
        return Collections.enumeration(set);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (HttpHeaders.COOKIE.equalsIgnoreCase(name) && customCookieHeader != null) {
            return Collections.enumeration(Collections.singletonList(customCookieHeader));
        }
        List<String> headerValues = Collections.list(super.getHeaders(name));
        if (customHeaders.containsKey(name)) {
            headerValues.add(0, customHeaders.get(name));
        }
        return Collections.enumeration(headerValues);
    }

    @Override
    public Cookie[] getCookies() {
        if (customCookies != null) {
            return customCookies;
        }
        return super.getCookies();
    }
}
