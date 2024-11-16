package com.nhnacademy.heukbaekfrontend.point.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointEarnStandardResponse(
        Long id,
        String name,
        BigDecimal point,
        PointEarnStandardStatus status,
        PointEarnType pointEarnType,
        LocalDateTime pointEarnStart,
        LocalDateTime pointEarnEnd,
        EventCode eventCode
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
