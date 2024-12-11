package com.nhnacademy.heukbaekfrontend.common.rabbitmq.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponRabbitMqConstants {
    public static final String COUPON_ISSUE_EXCHANGE = "coupon-issue.exchange";
    public static final String COUPON_ISSUE_ROUTING_KEY = "coupon-issue.key";
    public static final String COUPON_ISSUE_QUEUE = "coupon-issue.queue";

    public static final String COUPON_ISSUE_DEAD_LETTER_EXCHANGE = "coupon-issue.dead-letter.exchange";
    public static final String COUPON_ISSUE_DEAD_LETTER_ROUTING_KEY = "coupon-issue.dead-letter.key";
    public static final String COUPON_ISSUE_DEAD_LETTER_QUEUE = "coupon-issue.dead-letter.queue";
}
