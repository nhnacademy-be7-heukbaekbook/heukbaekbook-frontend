package com.nhnacademy.heukbaekfrontend.cart.dto;

import java.util.List;

public record CartResponse(
        List<CartBookResponse> cartBookResponses
) {
}
