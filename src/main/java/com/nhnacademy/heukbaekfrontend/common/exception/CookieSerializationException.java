package com.nhnacademy.heukbaekfrontend.common.exception;

public class CookieSerializationException extends RuntimeException {
    public CookieSerializationException(String message, Throwable e) {
        super(message, e);
    }
}
