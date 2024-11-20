package com.nhnacademy.heukbaekfrontend.point.client;

import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "pointHistoryClient", url = "http://localhost:8082/api/points")
public interface PointHistoryClient {
    @GetMapping("/balance")
    ResponseEntity<BigDecimal> getCurrentBalance();

    @GetMapping("/histories")
    ResponseEntity<Page<PointHistoryResponse>> getPointHistories(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String sort
    );
}
