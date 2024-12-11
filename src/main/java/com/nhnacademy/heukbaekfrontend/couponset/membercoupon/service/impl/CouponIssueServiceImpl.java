package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.impl;

import com.nhnacademy.heukbaekfrontend.common.rabbitmq.processor.RabbitMessageSender;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponIssueRequest;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.CouponIssueTimeException;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.CouponQuntatiyException;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.exception.DuplicatedCouponException;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.nhnacademy.heukbaekfrontend.common.rabbitmq.util.CouponRabbitMqConstants.COUPON_ISSUE_EXCHANGE;
import static com.nhnacademy.heukbaekfrontend.common.rabbitmq.util.CouponRabbitMqConstants.COUPON_ISSUE_ROUTING_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueServiceImpl implements CouponIssueService {

    private static final String COUPON_KEY = "coupon:%d";
    private static final String ISSUED_COUPON_KEY = "coupon:issue:%d";

    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitMessageSender rabbitMessageSender;

    @Override
    public String issueCoupon(Long couponId, Long customerId) {

        Map<Object, Object> couponEntries = redisTemplate.opsForHash().entries(COUPON_KEY.formatted(couponId));
        LocalDateTime requestTime = LocalDateTime.now();

        // 1. 발급 기간 유효 체크
        if (!checkCouponIssueTime(couponEntries, requestTime)) {
            throw new CouponIssueTimeException("이미 종료된 쿠폰입니다");
        }

        // 2. 중복 발급 여부
        if (!isCouponIssuedDuplicated(ISSUED_COUPON_KEY.formatted(couponId), customerId)) {
            throw new DuplicatedCouponException("이미 발급 받은 쿠폰입니다");
        }

        // 3. 수량 검증 && 가능 시 -1
        validateAndDecreaseCouponQuantity(COUPON_KEY.formatted(couponId));
        log.debug("Coupon Quantity decreased successfully");

        // 4. Rabbit MQ, 쿠폰 히스토리 저장 메세지 생산
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId, customerId, (int)couponEntries.get("availableDuration"));
        rabbitMessageSender.sendMessage(COUPON_ISSUE_EXCHANGE, COUPON_ISSUE_ROUTING_KEY, couponIssueRequest);
        log.debug("Coupon produce successful");

        // 5. Redis, 쿠폰 발급 요청 기록 저장
        redisTemplate.opsForSet().add(ISSUED_COUPON_KEY.formatted(couponId), customerId);
        log.debug("Coupon Issued Recorded in Redis successfully");

        return "쿠폰 발급이 요청되었습니다.";
    }


    private boolean checkCouponIssueTime(Map<Object, Object> couponEntries, LocalDateTime requestTime) {
        LocalDateTime couponTimeStart = LocalDateTime.parse((String)couponEntries.get("couponTimeStart"));
        LocalDateTime couponTimeEnd = LocalDateTime.parse((String)couponEntries.get("couponTimeEnd"));

        return couponTimeStart.isBefore(requestTime) && requestTime.isBefore(couponTimeEnd);
    }


    private void validateAndDecreaseCouponQuantity(String couponKey){
        // couponQuantity 감소
        Long updatedQuantity = redisTemplate.opsForHash().increment(couponKey, "couponQuantity", -1);

        if (updatedQuantity < 0) {
            redisTemplate.opsForHash().increment(couponKey, "couponQuantity", 1); // 롤백
            throw new CouponQuntatiyException("쿠폰 수량이 부족합니다.");
        }
    }

    private boolean isCouponIssuedDuplicated(String issuedCouponKey, Long customerId) {
        return redisTemplate.opsForSet().isMember(issuedCouponKey, customerId);
    }


}