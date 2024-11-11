package com.nhnacademy.heukbaekfrontend.common.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(String title, int status, ZonedDateTime timestamp) {

}
