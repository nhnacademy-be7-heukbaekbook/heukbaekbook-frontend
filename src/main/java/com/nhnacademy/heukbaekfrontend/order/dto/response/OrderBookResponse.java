package com.nhnacademy.heukbaekfrontend.order.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class OrderBookResponse {
    private Long id;
    private String thumbnailUrl;
    private String title;
    private String price;
    private int quantity;
    private String salePrice;
    private BigDecimal discountRate;
    private String totalPrice;
    @Setter
    private boolean hasReview;

}
