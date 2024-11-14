package com.nhnacademy.heukbaekfrontend.point.dto;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record PointEarnStandardResponse(
        Long id,
        String name,
        BigDecimal point,
        PointEarnStandardStatus status,
        PointEarnTriggerEvent triggerEvent
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
