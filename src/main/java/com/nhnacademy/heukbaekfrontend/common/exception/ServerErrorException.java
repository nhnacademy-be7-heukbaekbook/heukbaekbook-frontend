package com.nhnacademy.heukbaekfrontend.common.exception;

import java.util.Objects;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException(String message) {
        super(Objects.isNull(message) ? "죄송합니다. 서버에 오류가 발생하였습니다." : message);
    }
}
