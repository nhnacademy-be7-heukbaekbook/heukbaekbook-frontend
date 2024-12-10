package com.nhnacademy.heukbaekfrontend.common.util;

import com.nhnacademy.heukbaekfrontend.common.exception.CookieSerializationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Component
public class CookieUtil {

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void deleteAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    public void addCookie(HttpServletResponse response, String name, String value, long expiryInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setMaxAge((int) expiryInSeconds);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    public <T> String serialize(T object) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return Base64.getUrlEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            throw new CookieSerializationException("Failed to serialize object", e);
        }
    }

    public <T> T deserialize(String cookieValue, Class<T> clazz) {
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(Base64.getUrlDecoder().decode(cookieValue));
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            return clazz.cast(objectStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new CookieSerializationException("Failed to deserialize object", e);
        }
    }
}
