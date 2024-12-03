package com.nhnacademy.heukbaekfrontend.point.service.impl;

import com.nhnacademy.heukbaekfrontend.point.client.PointHistoryClient;
import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import com.nhnacademy.heukbaekfrontend.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final PointHistoryClient pointHistoryClient;

    @Override
    public BigDecimal getCurrentPointBalance() {
        ResponseEntity<BigDecimal> currentBalance = pointHistoryClient.getCurrentBalance();

        return currentBalance.getBody();
    }

    @Override
    public Page<PointHistoryResponse> getPointHistories(Pageable pageable) {
        ResponseEntity<Page<PointHistoryResponse>> pointHistories = pointHistoryClient.getPointHistories(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "createdAt,desc"
        );

        return pointHistories.getBody();
    }
}
