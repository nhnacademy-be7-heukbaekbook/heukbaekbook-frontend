package com.nhnacademy.heukbaekfrontend.point.service;

import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PointHistoryService {

    BigDecimal getCurrentPointBalance();

    Page<PointHistoryResponse> getPointHistories(Pageable pageable);
}
