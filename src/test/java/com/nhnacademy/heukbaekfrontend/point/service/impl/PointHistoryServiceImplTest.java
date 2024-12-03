package com.nhnacademy.heukbaekfrontend.point.service.impl;

import com.nhnacademy.heukbaekfrontend.point.client.PointHistoryClient;
import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import com.nhnacademy.heukbaekfrontend.point.dto.PointType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PointHistoryServiceImplTest {

    @InjectMocks
    private PointHistoryServiceImpl pointHistoryService;

    @Mock
    private PointHistoryClient pointHistoryClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentPointBalance_Success() {
        // Given
        BigDecimal mockBalance = BigDecimal.valueOf(5000);
        when(pointHistoryClient.getCurrentBalance())
                .thenReturn(new ResponseEntity<>(mockBalance, HttpStatus.OK));

        // When
        BigDecimal result = pointHistoryService.getCurrentPointBalance();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockBalance);
        verify(pointHistoryClient, times(1)).getCurrentBalance();
    }

    @Test
    void testGetPointHistories_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PointHistoryResponse mockHistory = new PointHistoryResponse(
                1L,
                123L,
                456L,
                "Order Points",
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                BigDecimal.valueOf(5000),
                PointType.EARNED
        );
        Page<PointHistoryResponse> mockPage = new PageImpl<>(List.of(mockHistory));

        when(pointHistoryClient.getPointHistories(anyInt(), anyInt(), anyString()))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        // When
        Page<PointHistoryResponse> result = pointHistoryService.getPointHistories(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Order Points");
        verify(pointHistoryClient, times(1))
                .getPointHistories(anyInt(), anyInt(), eq("createdAt,desc"));
    }
}
